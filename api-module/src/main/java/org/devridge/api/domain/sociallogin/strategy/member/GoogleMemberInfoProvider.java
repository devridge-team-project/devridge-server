package org.devridge.api.domain.sociallogin.strategy.member;

import lombok.RequiredArgsConstructor;
import org.devridge.api.domain.sociallogin.config.GoogleOAuth2Properties;
import org.devridge.api.domain.sociallogin.dto.response.OAuth2TokenResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class GoogleMemberInfoProvider implements OAuth2MemberInfoStrategy {

    private final GoogleOAuth2Properties googleOAuth2Properties;

    @Override
    public Map<String, Object> getMemberInfo(OAuth2TokenResponse tokenResponse) {
        Map<String, Object> userAttributes = WebClient.create()
                .get()
                .uri(googleOAuth2Properties.getUserInfoEndpoint())
                .headers(header-> header.setBearerAuth(tokenResponse.getAccessToken()))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .block();
        return userAttributes;
    }
}