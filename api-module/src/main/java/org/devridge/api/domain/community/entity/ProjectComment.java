package org.devridge.api.domain.community.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devridge.api.common.entity.BaseEntity;
import org.devridge.api.domain.member.entity.Member;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@DynamicInsert
@SQLDelete(sql = "UPDATE project_comment SET is_deleted = true WHERE id = ?")
@Where(clause = "is_deleted = false")
public class ProjectComment extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", updatable = false)
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", updatable = false)
    private Member member;

    private String content;

    @ColumnDefault("0")
    private Long likes;

    @ColumnDefault("0")
    private Long dislikes;

    @Builder
    public ProjectComment(Project project, Member member, String content) {
        this.project = project;
        this.member = member;
        this.content = content;
    }
}