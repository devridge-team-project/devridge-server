package org.devridge.api.domain.sociallogin.entity;

import java.util.Map;

public class KakaoMember implements OAuth2Member {

    private Map<String, Object> properties;
    private Map<String, Object> kakaoAccount;

    public KakaoMember(Map<String, Object> attributes) {
        this.properties = (Map)attributes.get("properties");
        this.kakaoAccount = (Map)attributes.get("kakao_account");
    }

    @Override
    public String getProvider() {
        return "kakao";
    }

    @Override
    public String getEmail() {
        return (String)kakaoAccount.get("email");
    }

    @Override
    public String getProfileImageUrl() {
        return (String)properties.get("profile_image");
    }
}
