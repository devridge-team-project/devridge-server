package org.devridge.api.domain.sociallogin.strategy.member;

import org.devridge.api.domain.sociallogin.dto.response.oauth.OAuth2TokenResponse;

import java.util.Map;

public interface OAuth2MemberInfoStrategy {

    Map<String, Object> getMemberInfo(OAuth2TokenResponse tokenResponse);
}