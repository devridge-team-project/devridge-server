package org.devridge.api.common.security.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.devridge.api.common.exception.common.DataNotFoundException;
import org.devridge.api.common.security.auth.CustomMemberDetails;
import org.devridge.api.common.util.ResponseUtil;
import org.devridge.api.domain.member.entity.Member;
import org.devridge.api.infrastructure.auth.RefreshTokenRepository;
import org.devridge.api.infrastructure.member.MemberRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.devridge.api.common.util.AccessTokenUtil.extractAccessTokenFromCookies;
import static org.devridge.api.common.util.AccessTokenUtil.getClaimsFromAccessToken;
import static org.devridge.api.common.util.JwtUtil.createAccessToken;
import static org.devridge.api.common.util.JwtUtil.generateAccessTokenCookie;
import static org.devridge.api.common.util.RefreshTokenUtil.checkIfRefreshTokenValid;
import static org.devridge.api.common.util.RefreshTokenUtil.extractRefreshTokenFromCookies;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
    private MemberRepository memberRepository;
    private RefreshTokenRepository refreshTokenRepository;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, MemberRepository memberRepository, RefreshTokenRepository refreshTokenRepository) {
        super(authenticationManager);
        this.memberRepository = memberRepository;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    // 1. RequestHeader 안의 엑세스 토큰 확인
    // 2. 액세스토큰이 유효하다면 -> 인증된 객체 저장하고 doFilter 수행

    private final List<RequestMatcher> excludedUrlPatterns = Arrays.asList(//이 필터 적용 안할 url 지정
            new AntPathRequestMatcher("/api/email-verifications"),
            new AntPathRequestMatcher("/api/users", "POST"),
            new AntPathRequestMatcher("/api/login", "POST"),
            new AntPathRequestMatcher("/api/users/reset-password", "PATCH"),
            new AntPathRequestMatcher("/api/auth/accessToken", "GET")
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (isExcludedUrl(request)) {
            filterChain.doFilter(request, response); //이 필터 스킵, 다음꺼 실행.
            return;
        }
        String accessToken = extractAccessTokenFromCookies(request);

        if (accessToken == null) {
            filterChain.doFilter(request, response);
            return;
        }
        Claims claims = null;

        try {
            claims = getClaimsFromAccessToken(accessToken);
        } catch (ExpiredJwtException e) {
            // 엑세스 토큰이 만료되었을 때 리프레시 토큰이 유효하다면, 엑세스 토큰을 새로 발급해줍니다.
            String refreshToken = extractRefreshTokenFromCookies(request);

            if (!checkIfRefreshTokenValid(refreshToken)) {
                handleExceptionResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "access-token expired");
                return;
            }
            claims = e.getClaims();
            Long refreshTokenId = ((Integer) claims.get("refreshTokenId")).longValue();

            Long memberId = ((Integer) claims.get("memberId")).longValue();
            Member member = memberRepository.findById(memberId).orElseThrow(() -> new DataNotFoundException());

            String newAccessToken = createAccessToken(member, refreshTokenId);

            ResponseCookie accessTokenCookie = generateAccessTokenCookie(newAccessToken);
            response.addHeader(HttpHeaders.SET_COOKIE, accessTokenCookie.toString());
        } catch (MalformedJwtException e) {
            filterChain.doFilter(request, response);
            return;
        }

        Member member = findMemberFromAccessTokenClaims(response, claims);
        this.saveAuthenticationToSecurityContextHolder(member);

        filterChain.doFilter(request, response);
    }

    private Member findMemberFromAccessTokenClaims(HttpServletResponse response, Claims claims) throws IOException {
        Optional<Member> savedMember = memberRepository.findByEmailAndProvider(
                claims.get("memberEmail").toString(),
                claims.get("provider").toString()
        );

        if (!savedMember.isPresent()) {
            ResponseUtil.createResponseBody(response, HttpStatus.UNAUTHORIZED);
            return null;
        }

        return savedMember.get();
    }

    private void saveAuthenticationToSecurityContextHolder(Member member) {
        CustomMemberDetails memberDetails = new CustomMemberDetails(member);

        // 인가 처리가 정상적으로 완료된다면 Authentication 객체 생성
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                memberDetails, null, memberDetails.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private boolean isExcludedUrl(HttpServletRequest request) {
        return excludedUrlPatterns.stream().anyMatch(pattern -> pattern.matches(request));
    }

    private void handleExceptionResponse(HttpServletResponse response, int status, String errorMessage) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(status);

        String json = String.format("{\"error\":\"%s\"}", errorMessage);
        response.getWriter().write(json);

        response.getWriter().flush();
        response.getWriter().close();
    }
}
