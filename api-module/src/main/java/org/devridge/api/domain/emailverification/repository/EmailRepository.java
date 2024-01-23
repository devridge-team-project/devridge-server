package org.devridge.api.domain.emailverification.repository;

import org.devridge.api.domain.emailverification.entity.EmailVerification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailRepository extends JpaRepository<EmailVerification, Long> {
}
