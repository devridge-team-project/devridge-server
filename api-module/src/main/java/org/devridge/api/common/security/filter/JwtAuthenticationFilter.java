package org.devridge.api.common.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.devridge.api.common.dto.BaseErrorResponse;
import org.devridge.api.common.security.auth.CustomMemberDetails;
import org.devridge.api.common.util.ResponseUtil;
import org.devridge.api.domain.auth.entity.RefreshToken;
import org.devridge.api.domain.member.entity.Member;
import org.devridge.api.domain.member.exception.WrongLoginException;
import org.devridge.api.infrastructure.auth.RefreshTokenRepository;
import org.devridge.api.infrastructure.skill.MemberSkillRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.devridge.api.common.util.JwtUtil.*;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private RefreshTokenRepository refreshTokenRepository;
    private MemberSkillRepository memberSkillRepository;

    public JwtAuthenticationFilter(RefreshTokenRepository refreshTokenRepository, MemberSkillRepository memberSkillRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.memberSkillRepository = memberSkillRepository;
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

        // 2. Refresh Token DB 저장 (해당 유저의 리프레시 토큰이 이미 존재한다면, 삭제 후 저장), 쿠키에 담아 반환
        String refreshToken = createRefreshToken(member);

        ResponseCookie refreshTokenCookie = generateRefreshTokenCookie(refreshToken);
        response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());

        Long refreshTokenId = saveRefreshToken(member, refreshToken);

        // 3. AccessToken 발급, 쿠키에 담아 보냄
        String accessToken = createAccessToken(member, refreshTokenId);

        ResponseCookie accessTokenCookie = generateAccessTokenCookie(accessToken);
        response.addHeader(HttpHeaders.SET_COOKIE, accessTokenCookie.toString());

        ResponseUtil.createResponseBody(response, HttpStatus.OK);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {
        BaseErrorResponse errorResponse = new BaseErrorResponse("email, password 가 일치하지 않습니다.");
        ResponseUtil.createResponseBody(response, errorResponse, HttpStatus.BAD_REQUEST);
    }

    private Long saveRefreshToken(Member member, String refreshToken) {
        try{
            refreshTokenRepository.findByMemberId(member.getId())
                    .ifPresent(refreshTokenRepository::delete);

            RefreshToken newRefreshToken = new RefreshToken(refreshToken, member);

            return refreshTokenRepository.save(newRefreshToken).getId();
        } catch (NullPointerException e) {
            throw new AuthenticationServiceException("유효하지 않은 사용자입니다.");
        }
    }
}
