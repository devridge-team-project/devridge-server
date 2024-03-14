package org.devridge.api.domain.note.entity;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devridge.api.common.entity.BaseEntity;
import org.devridge.api.domain.community.entity.Project;
import org.devridge.api.domain.member.entity.Member;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

@Entity
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ProjectParticipationNote extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    private Member sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id")
    private Member receiver;

    @NotNull
    private String content;

    @Column(columnDefinition = "TINYINT(1)")
    private Boolean isApproved;

    private LocalDateTime readAt;

    @Column(columnDefinition = "TINYINT(1)")
    @ColumnDefault("false")
    private Boolean deletedBySender;

    @Column(columnDefinition = "TINYINT(1)")
    @ColumnDefault("false")
    private Boolean deletedByReceiver;

    @Builder
    public ProjectParticipationNote(Project project, Member sender, Member receiver, String content) {
        this.project = project;
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
    }

    public void deleteBySender() {
        this.deletedBySender = true;
    }

    public void deleteByReceiver() {
        this.deletedByReceiver = true;
    }

    public boolean isDeletedBySender() {
        return this.deletedBySender;
    }
    public boolean isDeletedByReceiver() {
        return this.deletedByReceiver;
    }

    public boolean isDeleted() {
        return isDeletedBySender() && isDeletedByReceiver();
    }

    public void updateReadAt() {
        this.readAt = LocalDateTime.now();
    }

    public void updateIsApproved(Boolean result) {
        if (result) {
            this.isApproved = true;
        }
        if (!result) {
            this.isApproved = false;
        }
    }
}
