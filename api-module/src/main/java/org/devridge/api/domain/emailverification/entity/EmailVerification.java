package org.devridge.api.domain.emailverification.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devridge.api.common.entity.BaseEntity;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "email_verification")
@Getter
@DynamicInsert
@SQLDelete(sql = "UPDATE email_verification SET is_deleted = true WHERE id = ?")
@Where(clause = "is_deleted = false")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmailVerification extends BaseEntity {

    private String receiptEmail;

    private String content;

    private boolean checkStatus;

    private LocalDateTime expireAt;

    @Builder
    public EmailVerification(String receiptEmail, String content, boolean checkStatus, LocalDateTime expireAt) {
        this.receiptEmail = receiptEmail;
        this.content = content;
        this.checkStatus = checkStatus;
        this.expireAt = expireAt;
    }

    public void changeCheckStatus() {
        this.checkStatus = true;
    }
}
