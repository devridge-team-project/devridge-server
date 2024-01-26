package org.devridge.api.domain.emailverification.service;

import lombok.RequiredArgsConstructor;
import org.devridge.api.domain.emailverification.dto.request.SendEmailRequest;
import org.devridge.api.domain.emailverification.entity.EmailVerification;
import org.devridge.api.domain.emailverification.repository.EmailVerificationRepository;
import org.devridge.api.exception.email.EmailVerificationInvalidException;
import org.devridge.api.util.RandomGeneratorUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class EmailVerificationService {

    private final EmailVerificationRepository emailVerificationRepository;
    private final EmailMessageSender emailMessageSender;
    private final TemplateEngine templateEngine;

    @Value("${devridge.email.expire-minutes.signup}")
    private int EMAIL_EXP_MINUTES;

    @Value("${devridge.email.expire-minutes.signup}")
    private int VERIFICATION_COMPLETION_EFFECTIVE_TIME;

    public void sendVerificationEmail(SendEmailRequest emailVerificationRequest) {
        EmailVerification emailVerification = createEmailVerification(emailVerificationRequest.getEmail(), EMAIL_EXP_MINUTES);
        emailVerificationRepository.save(emailVerification);

        Context context = new Context();
        context.setVariable("verificationCode", emailVerification.getContent());

        String htmlContent = templateEngine.process("verificationEmailTemplate", context);

        emailMessageSender.sendSimpleMessage(
                emailVerification.getReceiptEmail(),
                "[devridge] 가입 인증번호 안내",
                htmlContent
        );
    }

    private EmailVerification createEmailVerification(String email, int expMinutes) {
        String content = String.valueOf(RandomGeneratorUtil.generateFourDigitNumber());
        LocalDateTime expiredAt = LocalDateTime.now().plusMinutes(expMinutes);

        return EmailVerification.builder()
                .receiptEmail(email)
                .content(content)
                .checkStatus(false)
                .expireAt(expiredAt)
                .build();
    }

    public void checkVerificationCode(String email, String code) {
        EmailVerification emailVerification = emailVerificationRepository.findTopByReceiptEmailOrderByCreatedAtDesc(email)
                .orElseThrow(() -> new EmailVerificationInvalidException());
        System.out.println("emailVerification = " + emailVerification);
        
        LocalDateTime current = LocalDateTime.now();

        validateEmailVerification(emailVerification, current);
        validateVerificationCode(code, emailVerification.getContent());

        emailVerificationRepository.save(emailVerification);
    }

    private static void validateEmailVerification(EmailVerification emailVerification, LocalDateTime current) {
        if (emailVerification.isCheckStatus() == true) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "유효하지 않은 인증 번호입니다.");
        }
        if (emailVerification.getExpireAt().isBefore(current)) {
            throw new ResponseStatusException(HttpStatus.GONE, "유효하지 않은 인증 번호입니다.");
        }
    }

    public void validateVerificationCode(String code, String content) {
        if (!content.equals(code)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "인증 번호가 일치하지 않습니다.");
        }
    }
}
