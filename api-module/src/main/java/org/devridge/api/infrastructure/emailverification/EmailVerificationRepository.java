package org.devridge.api.infrastructure.emailverification;

import org.devridge.api.domain.emailverification.entity.EmailVerification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailVerificationRepository extends JpaRepository<EmailVerification, Long> {

    Optional<EmailVerification> findTopByReceiptEmailOrderByCreatedAtDesc(String receiptEmail);
}