package org.devridge.api.domain.auth.controller;

import lombok.RequiredArgsConstructor;
import org.devridge.api.domain.auth.service.AuthService;
import org.devridge.api.common.security.dto.TokenResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @GetMapping("/accessToken")
    public ResponseEntity<TokenResponse> reIssueAccessToken(HttpServletRequest request) {
        TokenResponse result = authService.reIssueAccessToken(request);
        return ResponseEntity.ok().body(result);
    }
}
