package org.devridge.api.domain.sociallogin.strategy.token;

import org.devridge.api.domain.sociallogin.dto.request.SocialLoginRequest;
import org.devridge.api.domain.sociallogin.dto.response.OAuth2TokenResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

import java.nio.charset.StandardCharsets;
import java.util.Collections;

@Component
public class NaverTokenProvider implements OAuth2TokenStrategy {

    private final ClientRegistration clientRegistration;

    @Autowired
    public NaverTokenProvider(ClientRegistrationRepository clientRegistrationRepository) {
        this.clientRegistration = clientRegistrationRepository.findByRegistrationId("naver");
    }

    @Override
    public OAuth2TokenResponse getToken(SocialLoginRequest loginRequest) {
        MultiValueMap<String, String> formData = createFormData(loginRequest);

        return WebClient.create()
                .post()
                .uri(clientRegistration.getProviderDetails().getTokenUri())
                .headers(header -> {
                    header.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                    header.setAcceptCharset(Collections.singletonList(StandardCharsets.UTF_8));
                })
                .bodyValue(formData)
                .retrieve()
                .bodyToMono(OAuth2TokenResponse.class)
                .block();
    }

    private MultiValueMap<String, String> createFormData(SocialLoginRequest loginRequest) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();

        formData.add("grant_type", "authorization_code");
        formData.add("client_id", clientRegistration.getClientId());
        formData.add("client_secret", clientRegistration.getClientSecret());
        formData.add("redirect_uri", clientRegistration.getRedirectUri());
        formData.add("code", loginRequest.getCode());

        return formData;
    }
}