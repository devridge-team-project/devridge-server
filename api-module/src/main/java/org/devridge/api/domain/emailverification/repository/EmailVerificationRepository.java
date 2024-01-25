package org.devridge.api.domain.emailverification.repository;

import org.devridge.api.constant.EmailVerificationContentType;
import org.devridge.api.domain.emailverification.entity.EmailVerification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface EmailVerificationRepository extends JpaRepository<EmailVerification, Long> {

    @Query("SELECT emailVerification FROM EmailVerification emailVerification " +
            "WHERE emailVerification.receiptEmail = :receiptEmail " +
            "AND emailVerification.contentType = :contentType AND emailVerification.isDeleted = false " +
            "ORDER BY emailVerification.createdAt DESC")
    Optional<EmailVerification> findLatestByReceiptEmailAndContentType(@Param("receiptEmail") String receiptEmail, @Param("contentType") EmailVerificationContentType contentType);
}