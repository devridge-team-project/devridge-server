package org.devridge.api.domain.sociallogin.strategy.member;

import org.devridge.api.domain.sociallogin.dto.response.oauth.OAuth2TokenResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Component
public class KakaoMemberInfoProvider implements OAuth2MemberInfoStrategy {

    private final ClientRegistration clientRegistration;

    @Autowired
    public KakaoMemberInfoProvider(ClientRegistrationRepository clientRegistrationRepository) {
        this.clientRegistration = clientRegistrationRepository.findByRegistrationId("kakao");
    }

    @Override
    public Map<String, Object> getMemberInfo(OAuth2TokenResponse tokenResponse) {
        Map<String, Object> userAttributes = WebClient.create()
                .get()
                .uri(clientRegistration.getProviderDetails().getUserInfoEndpoint().getUri())
                .headers(header-> header.setBearerAuth(tokenResponse.getAccessToken()))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .block();
        return userAttributes;
    }
}