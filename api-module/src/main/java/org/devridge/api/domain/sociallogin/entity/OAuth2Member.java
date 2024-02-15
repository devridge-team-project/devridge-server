package org.devridge.api.domain.sociallogin.entity;

public interface OAuth2Member {

    String getProvider();
    String getEmail();
    String getProfileImageUrl();
}
