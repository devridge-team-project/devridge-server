package org.devridge.api.domain.sociallogin.context;

import org.devridge.api.domain.sociallogin.dto.response.oauth.OAuth2TokenResponse;
import org.devridge.api.domain.sociallogin.entity.GoogleMember;
import org.devridge.api.domain.sociallogin.entity.KakaoMember;
import org.devridge.api.domain.sociallogin.entity.NaverMember;
import org.devridge.api.domain.sociallogin.entity.OAuth2Member;
import org.devridge.api.domain.sociallogin.strategy.member.GoogleMemberInfoProvider;
import org.devridge.api.domain.sociallogin.strategy.member.KakaoMemberInfoProvider;
import org.devridge.api.domain.sociallogin.strategy.member.NaverMemberInfoProvider;
import org.devridge.api.domain.sociallogin.strategy.member.OAuth2MemberInfoStrategy;
import org.devridge.api.domain.sociallogin.exception.SocialLoginException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class OAuth2MemberInfoContext {

    private final Map<String, OAuth2MemberInfoStrategy> strategies;
    private final Map<String, Function<Map<String, Object>, OAuth2Member>> memberInfoProviderMap = new HashMap<>();

    @Autowired
    public OAuth2MemberInfoContext(
            NaverMemberInfoProvider naverMemberInfoProvider,
            GoogleMemberInfoProvider googleMemberInfoProvider,
            KakaoMemberInfoProvider kakaoMemberInfoProvider
    )
    {
        this.strategies = Map.of(
                "naver", naverMemberInfoProvider,
                "google", googleMemberInfoProvider,
                "kakao", kakaoMemberInfoProvider
        );

        memberInfoProviderMap.put("naver", NaverMember::new);
        memberInfoProviderMap.put("google", GoogleMember::new);
        memberInfoProviderMap.put("kakao", KakaoMember::new);
    }

    public Map<String, Object> getMemberInfoFromAuthServer(String provider, OAuth2TokenResponse tokenResponse) {
        OAuth2MemberInfoStrategy strategy = strategies.get(provider);

        if(strategy == null) {
            throw new SocialLoginException(400, "Unknown Provider: " + provider);
        }
        return strategy.getMemberInfo(tokenResponse);
    }

    public OAuth2Member makeOAuth2Member(String provider, Map<String, Object> userAttributes) {
        Function<Map<String, Object>, OAuth2Member> memberInfoProvider = memberInfoProviderMap.get(provider);

        if (memberInfoProvider == null) {
            throw new SocialLoginException(400, "Unknown Provider: " + provider);
        }
        return memberInfoProvider.apply(userAttributes);
    }
}
