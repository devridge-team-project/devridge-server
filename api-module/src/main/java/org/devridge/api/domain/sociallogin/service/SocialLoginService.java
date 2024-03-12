package org.devridge.api.domain.sociallogin.service;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.devridge.api.common.constant.Role;
import org.devridge.api.domain.auth.entity.RefreshToken;
import org.devridge.api.domain.auth.repository.RefreshTokenRepository;
import org.devridge.api.domain.member.entity.Member;
import org.devridge.api.domain.member.entity.Occupation;
import org.devridge.api.domain.member.exception.AccessTokenInvalidException;
import org.devridge.api.domain.member.exception.DuplNicknameException;
import org.devridge.api.domain.member.repository.MemberRepository;
import org.devridge.api.domain.member.repository.OccupationRepository;
import org.devridge.api.domain.sociallogin.context.OAuth2MemberInfoContext;
import org.devridge.api.domain.sociallogin.context.OAuth2TokenContext;
import org.devridge.api.domain.sociallogin.dto.request.SocialLoginRequest;
import org.devridge.api.domain.sociallogin.dto.request.SocialLoginSignUp;
import org.devridge.api.domain.sociallogin.dto.response.SocialLoginResponse;
import org.devridge.api.domain.sociallogin.dto.response.oauth.OAuth2TokenResponse;
import org.devridge.api.domain.sociallogin.entity.OAuth2Member;
import org.devridge.api.common.exception.common.DataNotFoundException;
import org.devridge.api.common.security.dto.TokenResponse;
import org.devridge.api.common.util.AccessTokenUtil;
import org.devridge.api.common.util.JwtUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SocialLoginService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;
    private final OccupationRepository occupationRepository;

    private final OAuth2TokenContext tokenProviderContext;
    private final OAuth2MemberInfoContext oAuth2MemberInfoContext;

    public SocialLoginResponse signIn(SocialLoginRequest socialLoginRequest, HttpServletResponse response) {
        OAuth2TokenResponse tokenResponse = tokenProviderContext.getTokenFromAuthServer(socialLoginRequest);

        Map<String, Object> memberInfoFromAuthServer = oAuth2MemberInfoContext.getMemberInfoFromAuthServer(
                socialLoginRequest.getProvider(), tokenResponse
        );

        OAuth2Member oAuth2MemberInfo = oAuth2MemberInfoContext.makeOAuth2Member(socialLoginRequest.getProvider(), memberInfoFromAuthServer);
        Optional<Member> existingMember = memberRepository.findByEmailAndProvider(oAuth2MemberInfo.getEmail(), oAuth2MemberInfo.getProvider());

        if (existingMember.isPresent()) {
            RefreshToken refreshToken = saveRefreshToken(existingMember.get());
            String accessToken = JwtUtil.createAccessToken(existingMember.get(), refreshToken.getId());

            ResponseCookie responseCookie = JwtUtil.generateRefreshTokenCookie(refreshToken.getRefreshToken());
            response.addHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());

            return new SocialLoginResponse(accessToken, false);
        }

        String tempJwt = JwtUtil.createTemporaryJwt(oAuth2MemberInfo);

        return new SocialLoginResponse(tempJwt, true);
    }

    private RefreshToken saveRefreshToken(Member member) {
        String refreshToken = JwtUtil.createRefreshToken(member);

        refreshTokenRepository.findByMemberId(member.getId())
                .ifPresent(refreshTokenRepository::delete);

        RefreshToken newRefreshToken = RefreshToken.builder()
                .refreshToken(refreshToken)
                .member(member)
                .build();

        return refreshTokenRepository.save(newRefreshToken);
    }

    // TODO : skillId, occupationId 검사 : 메모리
    @Transactional
    public TokenResponse signUpAndLogin(SocialLoginSignUp socialLoginRequest, HttpServletResponse response) {
        checkDuplNickname(socialLoginRequest.getNickname());

        Claims claims = checkTemporaryTokenForSocialLogin(socialLoginRequest.getTempJwt());
        Member member = createMember(socialLoginRequest, claims);

        RefreshToken refreshToken = saveRefreshToken(member);
        String accessToken = JwtUtil.createAccessToken(member, refreshToken.getId());

        ResponseCookie responseCookie = JwtUtil.generateRefreshTokenCookie(refreshToken.getRefreshToken());
        response.addHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());

        return new TokenResponse(accessToken);
    }

    private Member createMember(SocialLoginSignUp socialLoginRequest, Claims claims) {
        String memberEmail = (String) claims.get("memberEmail");
        String provider = (String) claims.get("provider");

        Member member = createMember(socialLoginRequest, memberEmail, provider);
        return member;
    }

    private void checkDuplNickname(String nickname) {
        if (memberRepository.findByNickname(nickname).isPresent()) {
            throw new DuplNicknameException(409, "이미 존재하는 닉네임입니다. 다른 닉네임을 사용해주세요.");
        }
    }

    private static Claims checkTemporaryTokenForSocialLogin(String temporaryToken) {
        try {
            Claims claims = AccessTokenUtil.getClaimsFromAccessToken(temporaryToken);

            if (!claims.get("purpose").equals("social-login")) {
                throw new AccessTokenInvalidException(403, "잘못된 토큰입니다.");
            }
            return claims;
        } catch (Exception e) {
            throw new AccessTokenInvalidException(401, "로그아웃 되었습니다. 다시 로그인해주세요.");
        }
    }

    private Member createMember(SocialLoginSignUp request, String memberEmail, String provider) {
        String password = UUID.randomUUID().toString();
        String encodedPassword = passwordEncoder.encode(password);

        Occupation occupation = occupationRepository.findById(request.getOccupationId()).orElseThrow(
                () -> new DataNotFoundException());

        Member member = Member.builder()
                .email(memberEmail)
                .password(encodedPassword)
                .roles(Role.ROLE_USER)
                .introduction(request.getIntroduction())
                .nickname(request.getNickname())
                .provider(provider)
                .profileImageUrl(request.getProfileImageUrl())
                .occupation(occupation)
                .build();

        return memberRepository.save(member);
    }
}