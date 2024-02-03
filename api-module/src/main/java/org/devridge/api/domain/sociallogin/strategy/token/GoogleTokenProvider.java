package org.devridge.api.domain.sociallogin.strategy.token;

import lombok.RequiredArgsConstructor;
import org.devridge.api.domain.sociallogin.config.GoogleOAuth2Properties;
import org.devridge.api.domain.sociallogin.dto.request.SocialLoginRequest;
import org.devridge.api.domain.sociallogin.dto.response.OAuth2TokenResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class GoogleTokenProvider implements OAuth2TokenStrategy {

    private final GoogleOAuth2Properties googleOAuth2Properties;

    @Override
    public OAuth2TokenResponse getToken(SocialLoginRequest loginRequest) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();

        createFormData(loginRequest, formData);

        return WebClient.create().
                post()
                .uri(googleOAuth2Properties.getTokenUri())
                .bodyValue(formData)
                .retrieve()
                .bodyToMono(OAuth2TokenResponse.class)
                .block();
    }

    private void createFormData(SocialLoginRequest loginRequest, MultiValueMap<String, String> formData) {
        formData.add("code", loginRequest.getCode());
        formData.add("client_id", googleOAuth2Properties.getClientId());
        formData.add("client_secret", googleOAuth2Properties.getClientSecret());
        formData.add("redirect_uri", googleOAuth2Properties.getRedirectUri());
        formData.add("grant_type", googleOAuth2Properties.getGrantType());
    }
}