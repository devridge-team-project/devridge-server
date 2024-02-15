package org.devridge.api.domain.sociallogin.entity;

import java.util.Map;

public class GoogleMember implements OAuth2Member {

    private Map<String, Object> attributes;

    public GoogleMember(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getProvider() {
        return "google";
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }

    @Override
    public String getProfileImageUrl() {
        return (String) attributes.get("picture");
    }
}
