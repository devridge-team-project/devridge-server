package org.devridge.api.domain.sociallogin.controller;

import lombok.RequiredArgsConstructor;
import org.devridge.api.domain.sociallogin.dto.request.SocialLoginRequest;
import org.devridge.api.domain.sociallogin.dto.request.SocialLoginSignUp;
import org.devridge.api.domain.sociallogin.dto.response.SocialLoginRedirect;
import org.devridge.api.domain.sociallogin.dto.response.SocialLoginResponse;
import org.devridge.api.domain.sociallogin.service.SocialLoginService;
import org.devridge.api.common.security.dto.TokenResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/social-login")
@RequiredArgsConstructor
public class SocialLoginController {

    private final SocialLoginService socialLoginService;

    @PostMapping
    public ResponseEntity<?> signIn(@RequestBody @Valid SocialLoginRequest socialLoginRequest, HttpServletResponse response) {
       SocialLoginResponse data = socialLoginService.signIn(socialLoginRequest, response);

       if (data.isRedirect()) {  // 첫 소셜 로그인 : 리다이렉트 필요
           SocialLoginRedirect result = new SocialLoginRedirect(data.getToken());
           return ResponseEntity.status(HttpStatus.MULTIPLE_CHOICES).body(result);
       }

       return ResponseEntity.status(HttpStatus.OK).body(
               new TokenResponse(data.getToken())
       );
    }

    @PostMapping("/signUp")
    public ResponseEntity<TokenResponse> createMemberAndLogin(
            @RequestBody @Valid SocialLoginSignUp socialLoginRequest, HttpServletResponse response)
    {
        TokenResponse result = socialLoginService.signUpAndLogin(socialLoginRequest, response);
        return ResponseEntity.ok().body(result);
    }
}
