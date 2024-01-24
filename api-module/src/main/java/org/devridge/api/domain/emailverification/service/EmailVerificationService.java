package org.devridge.api.domain.emailverification.service;

import lombok.RequiredArgsConstructor;
import org.devridge.api.constant.EmailVerificationContentType;
import org.devridge.api.domain.emailverification.dto.request.EmailRequest;
import org.devridge.api.domain.emailverification.entity.EmailVerification;
import org.devridge.api.domain.emailverification.repository.EmailVerificationRepository;
import org.devridge.api.util.RandomGeneratorUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
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

    public void sendVerificationEmail(EmailRequest emailRequest) {
        EmailVerification emailVerification = createEmailVerification(emailRequest.getEmail(), EMAIL_EXP_MINUTES);
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
                .contentType(EmailVerificationContentType.SIGNUP)
                .content(content)
                .checkStatus(false)
                .expireAt(expiredAt)
                .build();
    }

}
