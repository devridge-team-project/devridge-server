package org.devridge.api.security.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import org.devridge.api.domain.member.entity.Member;
import org.devridge.api.domain.member.entity.RefreshToken;
import org.devridge.api.domain.member.repository.MemberRepository;
import org.devridge.api.domain.member.repository.RefreshTokenRepository;
import org.devridge.api.security.auth.AuthProperties;
import org.devridge.api.security.auth.CustomMemberDetails;
import org.devridge.api.security.auth.CustomMemberDetailsService;
import org.devridge.api.security.dto.TokenResponse;
import org.devridge.api.util.AccessTokenUtil;
import org.devridge.api.util.JwtUtil;
import org.devridge.api.util.ResponseUtil;
import org.devridge.common.dto.BaseResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
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
import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
    private MemberRepository memberRepository;
    private RefreshTokenRepository refreshTokenRepository;
    private static final Logger logger = LoggerFactory.getLogger(CustomMemberDetailsService.class);

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, MemberRepository memberRepository, RefreshTokenRepository refreshTokenRepository) {
        super(authenticationManager);
        this.memberRepository = memberRepository;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    // 1. RequestHeader 안의 엑세스 토큰 확인
    // 2. 액세스토큰이 유효하다면 -> 인증된 객체 저장하고 doFilter, 그렇지 않다면 -> 리프레시 토큰 검사
    // 3. DB 에서 리프레시토큰 조회. 리프레시 토큰이 유효하다면 -> 새로운 액세스토큰 발급, 그렇지 않다면 -> 인증된 객체를 저장하지 않고 doFilter

    private final List<RequestMatcher> excludedUrlPatterns = Arrays.asList(//이 필터 적용 안할 url 지정
            new AntPathRequestMatcher("/login/mailConfirm"),
            new AntPathRequestMatcher("/login/authenticate")
            // 추가적인 URL 패턴을 필요에 따라 설정
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException, UserPrincipalNotFoundException {

        if (isExcludedUrl(request)) {
            filterChain.doFilter(request, response); //이 필터 스킵, 다음꺼 실행.
            return;
        }

        String accessToken = null;

        try {
            accessToken = AccessTokenUtil.extractAccessTokenFromRequest(request);
        } catch (NullPointerException e){
            System.out.println("no access token");
            filterChain.doFilter(request, response);
            return;
        }

        boolean isAccessTokenExpired = false;
        Claims claims = null;

        try {
            claims = AccessTokenUtil.getClaimsFromAccessToken(accessToken);
        } catch (ExpiredJwtException e) {
            claims = e.getClaims();
            isAccessTokenExpired = true;
        } catch (MalformedJwtException e) {
            filterChain.doFilter(request, response);
            return;
        } catch (Exception e) {
            filterChain.doFilter(request, response);
            return;
        }

        Optional<Member> memberOpt = findMemberFromAccessTokenClaims(response, claims);
        if (!memberOpt.isPresent()) return;

        if (isAccessTokenExpired) {
            System.out.println("엑세스토큰이 만료되었습니다.");

            Long refreshTokenId = ((Integer)claims.get("refreshTokenId")).longValue();
            Optional<RefreshToken> refreshTokenOpt = refreshTokenRepository.findById(refreshTokenId);

            if (!refreshTokenOpt.isPresent()) {
                filterChain.doFilter(request, response);
                return;
            }
            // 리프레시 토큰이 살아있는 경우
            RefreshToken refreshToken = refreshTokenOpt.get();
            if (issueNewAccessToken(request, response, filterChain, refreshToken, memberOpt.get())) {
                return;
            }
        }

        this.saveAuthenticationToSecurityContextHolder(memberOpt.get());
        filterChain.doFilter(request, response);
    }

    private Optional<Member> findMemberFromAccessTokenClaims(HttpServletResponse response, Claims claims) throws IOException {
        Optional<Member> savedMember = memberRepository.findByEmailAndProvider(
                claims.get("memberEmail").toString(),
                claims.get("provider").toString()
        );

        if (!savedMember.isPresent()) {
            BaseResponse baseResponse = new BaseResponse(
                    HttpStatus.UNAUTHORIZED.value(),
                    "엑세스 토큰이 유효하지 않습니다."
            );
            ResponseUtil.createResponseMessage(response, baseResponse);
            return null;
        }

        return savedMember;
    }

    private boolean issueNewAccessToken(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain, RefreshToken refreshToken, Member savedMember) throws IOException, ServletException {
        Claims refreshTokenClaims = null;
        boolean hasErrorOccured = true;

        try {
            refreshTokenClaims =
                    Jwts.parserBuilder().setSigningKey(AuthProperties.getRefreshSecret()).build().parseClaimsJws(refreshToken.getRefreshToken()).getBody();
            hasErrorOccured = false;
        } catch (ExpiredJwtException e) {
            refreshTokenRepository.delete(refreshToken);

            BaseResponse baseResponse = new BaseResponse(
                    HttpStatus.UNAUTHORIZED.value(),
                    "재인증이 필요합니다."
            );
            ResponseUtil.createResponseMessage(response, baseResponse);
            return true;
        } catch (MalformedJwtException e) {
            filterChain.doFilter(request, response);
        } catch (Exception e) {     // TODO :: 구체 예외 처리
            filterChain.doFilter(request, response);
        }

        if (hasErrorOccured) {
            return true;
        }

        // 리프레시 토큰이 존재한다면 액세스토큰 재발급
        String newAccessToken = JwtUtil.createAccessToken(savedMember, refreshToken.getId());

        if (refreshTokenClaims != null) {
            BaseResponse baseResponse = new BaseResponse(
                    HttpStatus.OK.value(),
                    "엑세스토큰 재발급",
                    new TokenResponse(newAccessToken)
            );

            ResponseUtil.createResponseMessage(response, baseResponse);
        }
        return false;
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
}
