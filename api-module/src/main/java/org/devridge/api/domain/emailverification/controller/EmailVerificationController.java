package org.devridge.api.domain.emailverification.controller;

import lombok.RequiredArgsConstructor;
import org.devridge.api.domain.emailverification.dto.request.SendEmailRequest;
import org.devridge.api.domain.emailverification.service.EmailVerificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/email-verifications")
public class EmailVerificationController {

    private final EmailVerificationService emailVerificationService;

    @PostMapping
    public ResponseEntity<Void> sendVerificationEmail(
            @Valid @RequestBody SendEmailRequest sendEmailRequest
    ) {
        emailVerificationService.sendVerificationEmail(sendEmailRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<Void> checkVerificationCode(
            @RequestParam("email") String email,
            @RequestParam("code") String code
    ) {
        emailVerificationService.checkVerificationCode(email, code);
        return ResponseEntity.ok().build();
    }
}