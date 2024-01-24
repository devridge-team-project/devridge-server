package org.devridge.api.domain.emailverification.controller;

import lombok.RequiredArgsConstructor;
import org.devridge.api.domain.emailverification.dto.request.EmailRequest;
import org.devridge.api.domain.emailverification.dto.request.EmailVerificationCode;
import org.devridge.api.domain.emailverification.service.EmailVerificationService;
import org.devridge.api.domain.member.entity.Member;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/emails")
public class EmailVerificationController {

    private final EmailVerificationService emailVerificationService;

    @PostMapping("/verifications")
    public ResponseEntity<?> sendVerificationEmail(
            @Valid @RequestBody EmailRequest emailRequest
    ) {
        emailVerificationService.sendVerificationEmail(emailRequest);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/verifications")
    public ResponseEntity<?> verifyCode(
            @Valid @RequestBody EmailVerificationCode verificationCode
    ) {
        emailVerificationService.verifyEmailCode(verificationCode);
        return ResponseEntity.ok().build();
    }
}