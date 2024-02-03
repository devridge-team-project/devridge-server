package org.devridge.api.domain.sociallogin.controller;

import lombok.RequiredArgsConstructor;
import org.devridge.api.domain.sociallogin.dto.request.SocialLoginRequest;
import org.devridge.api.domain.sociallogin.dto.response.SocialLoginResponse;
import org.devridge.api.domain.sociallogin.service.SocialLoginService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/social-login")
@RequiredArgsConstructor
public class SocialLoginController {

    private final SocialLoginService socialLoginService;

    @PostMapping
    public ResponseEntity<SocialLoginResponse> signIn(
            @RequestBody @Valid SocialLoginRequest socialLoginRequest
    ) {
       SocialLoginResponse result = socialLoginService.signIn(socialLoginRequest);
       return ResponseEntity.ok().body(result);
    }
}
