package org.devridge.api.application.emailverification;

import lombok.RequiredArgsConstructor;

import org.devridge.api.domain.emailverification.dto.request.SendEmailRequest;
import org.devridge.api.domain.emailverification.entity.EmailVerification;
import org.devridge.api.infrastructure.emailverification.EmailVerificationRepository;
import org.devridge.api.domain.emailverification.exception.EmailVerificationInvalidException;
import org.devridge.api.common.util.JwtUtil;
import org.devridge.api.common.util.RandomGeneratorUtil;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    private final RandomGeneratorUtil randomGeneratorUtil;

    @Value("${devridge.email.expire-minutes.signup}")
    private int EMAIL_EXP_MINUTES;

    @Transactional
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
        String content = String.valueOf(randomGeneratorUtil.generateFourDigitNumber());
        LocalDateTime expiredAt = LocalDateTime.now().plusMinutes(expMinutes);

        return EmailVerification.builder()
                .receiptEmail(email)
                .content(content)
                .checkStatus(false)
                .expireAt(expiredAt)
                .build();
    }

    @Transactional
    public String checkVerificationCode(String email, String code) {
        EmailVerification emailVerification = emailVerificationRepository.findTopByReceiptEmailOrderByCreatedAtDesc(email)
                .orElseThrow(() -> new EmailVerificationInvalidException(404, "해당 데이터를 찾을 수 없습니다."));

        LocalDateTime current = LocalDateTime.now();

        validateEmailVerification(emailVerification, current);
        validateVerificationCode(code, emailVerification.getContent());

        emailVerification.changeCheckStatus();

        return JwtUtil.createTemporaryJwt(email);
    }

    private static void validateEmailVerification(EmailVerification emailVerification, LocalDateTime current) {
        if (emailVerification.isCheckStatus()) {
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
