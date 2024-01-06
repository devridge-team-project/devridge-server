package org.devridge.api.security.filter;

import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.devridge.api.domain.member.entity.Member;
import org.devridge.api.domain.member.entity.RefreshToken;
import org.devridge.api.domain.member.repository.RefreshTokenRepository;
import org.devridge.api.exception.member.WrongLoginException;
import org.devridge.api.security.auth.CustomMemberDetails;
import org.devridge.api.security.auth.CustomMemberDetailsService;
import org.devridge.api.security.dto.TokenResponse;
import org.devridge.api.util.JwtUtil;
import org.devridge.common.dto.BaseResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
    private static final Logger logger = LoggerFactory.getLogger(CustomMemberDetailsService.class);

    /*
        form 로그인이 아닌, 커스텀 로그인에서 api 요청시 인증 필터를 진행할 url
     */
    public JwtAuthenticationFilter(RefreshTokenRepository refreshTokenRepository, TokenResponse tokenResponse) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.tokenResponse = tokenResponse;
        setFilterProcessesUrl("/api/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws WrongLoginException {
        logger.debug("=== JwtAuthenticationFilter - attemptAuthentication() ====");
        try {
            // form으로 넘어온 값으로 member 객체를 생성
            Member member = new ObjectMapper().readValue(request.getReader(), Member.class);
            UsernamePasswordAuthenticationToken userToken = new UsernamePasswordAuthenticationToken(member.getEmail(), member.getPassword());
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
        logger.debug("=== JwtAuthenticationFilter - successfulAuthentication() ====");

        // 1. 로그인 성공된 유저 조회
        Member member = ((CustomMemberDetails) authResult.getPrincipal()).getMember();

        // 2. Refresh Token DB 저장 (해당 유저의 리프레시 토큰이 이미 존재한다면, 삭제 후 저장)
        String refreshToken = JwtUtil.createRefreshToken(member);
        Long refreshTokenId = saveRefreshToken(member, refreshToken);

        // 3. AccessToken 발급
        String accessToken = JwtUtil.createAccessToken(member, refreshTokenId);

        TokenResponse tokenResponse = this.tokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();

        BaseResponse baseResponse = new BaseResponse(
                HttpStatus.OK.value(),
                "login success",
                tokenResponse
        );

        createResponseMessage(response, baseResponse);
    }


    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {

        System.out.println("=== JwtAuthenticationFilter - unsuccessfulAuthentication() ====");
        // 1. Http Response Message 세팅 후 반환
        Object failedType = failed.getClass();

        BaseResponse baseResponse = null;

        // 2. 예외에 따른 response 세팅
        if(failedType.equals(BadCredentialsException.class) || failedType.equals(UsernameNotFoundException.class)) {

            baseResponse = new BaseResponse(
                    HttpStatus.UNAUTHORIZED.value(),
                    failed.getLocalizedMessage()
            );
            this.createResponseMessage(response, baseResponse);

        } else {
            baseResponse = new BaseResponse(
                    HttpStatus.BAD_REQUEST.value(),
                    failed.getLocalizedMessage()
            );
            this.createResponseMessage(response, baseResponse);
        }
    }

    private void createResponseMessage(HttpServletResponse response, BaseResponse baseResponse) throws StreamWriteException, DatabindException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        response.setContentType(MediaType.APPLICATION_JSON.toString());

        objectMapper.writeValue(response.getOutputStream(), baseResponse);
    }

    private Long saveRefreshToken(Member member, String refreshToken) {
        try{
            Optional<RefreshToken> refreshTokenOpt = refreshTokenRepository.findByMemberId(member.getId());

            refreshTokenRepository.findByMemberId(member.getId())
                            .ifPresent(refreshTokenRepository::delete);

            RefreshToken newRefreshToken = buildRefreshToken(member.getId(), refreshToken);

            return refreshTokenRepository.save(newRefreshToken).getId();
        }catch(NullPointerException e) {
            throw new AuthenticationServiceException("유효하지 않은 사용자입니다.");
        }
    }

    private static RefreshToken buildRefreshToken(Long memberId, String refreshToken) {
        RefreshToken newRefreshToken = RefreshToken.builder()
                .refreshToken(refreshToken)
                .memberId(memberId)
                .build();
        return newRefreshToken;
    }

}
