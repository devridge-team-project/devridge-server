package org.devridge.api.domain.coffeechat.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.devridge.api.domain.member.entity.Member;
import org.devridge.api.common.entity.BaseEntity;

import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

import javax.validation.constraints.NotNull;

import java.time.LocalDateTime;

@DynamicInsert
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "coffee_chat_request")
@Entity
public class CoffeeChatRequest extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_member_id")
    private Member fromMember;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_member_id")
    private Member toMember;

    @NotNull
    private String message;

    @Column(name = "is_success", columnDefinition = "TINYINT(1)")
    private Boolean isSuccess;

    private LocalDateTime readAt;

    public CoffeeChatRequest(Member fromMember, Member toMember, String message) {
        this.fromMember = fromMember;
        this.toMember = toMember;
        this.message = message;
    }

    public void updateSuccess(Boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public void updateReadAt() {
        this.readAt = LocalDateTime.now();
    }
}
