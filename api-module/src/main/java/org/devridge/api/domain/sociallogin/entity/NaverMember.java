package org.devridge.api.domain.sociallogin.entity;

import java.util.Map;

public class NaverMember implements OAuth2Member {

    private Map<String, Object> attributes;

    public NaverMember(Map<String, Object> attributes) {
        this.attributes = (Map) attributes.get("response");
    }

    @Override
    public String getProvider() {
        return "naver";
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }

    @Override
    public String getProfileImageUrl() {
        return (String) attributes.get("profile_image");
    }
}
