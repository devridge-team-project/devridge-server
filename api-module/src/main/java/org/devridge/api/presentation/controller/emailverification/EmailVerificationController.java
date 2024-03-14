package org.devridge.api.presentation.controller.emailverification;

import lombok.RequiredArgsConstructor;
import org.devridge.api.domain.emailverification.dto.request.SendEmailRequest;
import org.devridge.api.domain.emailverification.dto.response.EmailVerificationResponse;
import org.devridge.api.application.emailverification.EmailVerificationService;
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

    @PostMapping("/code")
    public ResponseEntity<EmailVerificationResponse> checkVerificationCode(
            @RequestParam("email") String email,
            @RequestParam("code") String code
    ) {
        String temporaryJwt = emailVerificationService.checkVerificationCode(email, code);
        EmailVerificationResponse emailResponse = new EmailVerificationResponse(temporaryJwt);

        return ResponseEntity.ok().body(emailResponse);
    }
}