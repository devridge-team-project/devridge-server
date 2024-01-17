package org.devridge.api.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.devridge.api.domain.member.entity.Member;
import org.devridge.api.domain.member.entity.RefreshToken;
import org.devridge.api.domain.member.repository.RefreshTokenRepository;
import org.devridge.api.exception.member.WrongLoginException;
import org.devridge.api.security.auth.CustomMemberDetails;
import org.devridge.api.security.dto.TokenResponse;
import org.devridge.api.util.JwtUtil;
import org.devridge.api.util.ResponseUtil;
import org.devridge.common.dto.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private RefreshTokenRepository refreshTokenRepository;
    private TokenResponse tokenResponse;

    public JwtAuthenticationFilter(RefreshTokenRepository refreshTokenRepository, TokenResponse tokenResponse) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.tokenResponse = tokenResponse;
        setFilterProcessesUrl("/api/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws WrongLoginException {
        try {
            // form으로 넘어온 값으로 member 객체를 생성
            Member member = new ObjectMapper().readValue(request.getReader(), Member.class);

            UsernamePasswordAuthenticationToken userToken =
                    new UsernamePasswordAuthenticationToken(member.getEmail(), member.getPassword());

            this.setDetails(request, userToken);

            // AuthenticationManager 에 인증을 위임한다.
            return getAuthenticationManager().authenticate(userToken);
        } catch (IOException e) {
            throw new AuthenticationServiceException("아이디와 비밀번호를 올바르게 입력해주세요.");
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        // 1. 로그인 성공된 유저 조회
        Member member = ((CustomMemberDetails) authResult.getPrincipal()).getMember();

        // 2. Refresh Token DB 저장 (해당 유저의 리프레시 토큰이 이미 존재한다면, 삭제 후 저장)
        String refreshToken = JwtUtil.createRefreshToken(member);
        Long refreshTokenId = saveRefreshToken(member, refreshToken);

        // 3. AccessToken 발급
        String accessToken = JwtUtil.createAccessToken(member, refreshTokenId);

        TokenResponse tokenResponse = this.tokenResponse.builder()
                .accessToken(accessToken)
                .build();

        BaseResponse baseResponse = new BaseResponse(
                HttpStatus.OK.value(),
                "login success",
                tokenResponse
        );

        ResponseUtil.createResponseMessage(response, baseResponse);
    }


    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {
        // 1. Http Response Message 세팅 후 반환
        Object failedType = failed.getClass();

        BaseResponse baseResponse = null;

        // 2. 예외에 따른 response 세팅
        if (failedType.equals(BadCredentialsException.class) || failedType.equals(UsernameNotFoundException.class)) {
            baseResponse = new BaseResponse(
                    HttpStatus.UNAUTHORIZED.value(),
                    failed.getLocalizedMessage()
            );
        }
        else {
            baseResponse = new BaseResponse(
                    HttpStatus.BAD_REQUEST.value(),
                    failed.getLocalizedMessage()
            );
        }

        ResponseUtil.createResponseMessage(response, baseResponse);
    }

    private Long saveRefreshToken(Member member, String refreshToken) {
        try{
            Optional<RefreshToken> refreshTokenOpt = refreshTokenRepository.findByMemberId(member.getId());

            if (refreshTokenOpt.isPresent()){
                refreshTokenRepository.delete(refreshTokenOpt.get());
            }
            RefreshToken newRefreshToken = buildRefreshToken(member, refreshToken);

            return refreshTokenRepository.save(newRefreshToken).getId();

        } catch (NullPointerException e) {
            throw new AuthenticationServiceException("유효하지 않은 사용자입니다.");
        }
    }

    private static RefreshToken buildRefreshToken(Member member, String refreshToken) {
        RefreshToken newRefreshToken = RefreshToken.builder()
                .refreshToken(refreshToken)
                .member(member)
                .build();

        return newRefreshToken;
    }

}