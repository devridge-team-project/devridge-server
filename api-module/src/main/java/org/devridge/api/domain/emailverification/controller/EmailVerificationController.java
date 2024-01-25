package org.devridge.api.domain.emailverification.controller;

import lombok.RequiredArgsConstructor;
import org.devridge.api.domain.emailverification.dto.request.CheckEmailVerification;
import org.devridge.api.domain.emailverification.service.EmailVerificationService;
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
            @Valid @RequestBody CheckEmailVerification sendEmailRequest
    ) {
        emailVerificationService.sendVerificationEmail(sendEmailRequest);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/verifications")
    public ResponseEntity<?> checkEmailVerification(
            @Valid @RequestBody CheckEmailVerification verificationRequest
    ) {
        emailVerificationService.checkEmailVerification(verificationRequest);
        return ResponseEntity.ok().build();
    }
}