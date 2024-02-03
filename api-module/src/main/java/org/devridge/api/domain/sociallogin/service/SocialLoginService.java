package org.devridge.api.domain.sociallogin.service;

import lombok.RequiredArgsConstructor;
import org.devridge.api.domain.sociallogin.dto.request.SocialLoginRequest;
import org.devridge.api.domain.sociallogin.dto.response.SocialLoginResponse;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SocialLoginService {

    public SocialLoginResponse signIn(SocialLoginRequest socialLoginRequest) {
    }
}
