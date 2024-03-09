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
import lombok.NoArgsConstructor;
import org.devridge.api.domain.community.entity.Project;
import org.devridge.api.domain.member.entity.Member;
import org.devridge.common.entity.BaseEntity;
import org.hibernate.annotations.DynamicInsert;

@Entity
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @Builder
    public ProjectParticipationNote(Project project, Member sender, Member receiver, String content) {
        this.project = project;
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
    }
}
