package org.devridge.api.domain.community.entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devridge.api.common.entity.BaseTimeEntity;
import org.devridge.api.domain.community.entity.id.CommunityLikeDislikeId;
import org.devridge.api.domain.community.entity.id.ProjectLikeDislikeId;
import org.devridge.api.domain.member.entity.Member;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.SQLDelete;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@DynamicInsert
@SQLDelete(sql = "UPDATE project_like_dislike SET is_deleted = true WHERE member_id = ? AND project_id = ?")
public class ProjectLikeDislike extends BaseTimeEntity {

    @EmbeddedId
    private ProjectLikeDislikeId id;

    @MapsId("memberId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", insertable = false, updatable = false)
    private Member member;


    @MapsId("projectId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", insertable = false, updatable = false)
    private Project project;

    @Enumerated(EnumType.STRING)
    private LikeStatus status;

    @Builder
    public ProjectLikeDislike(CommunityLikeDislikeId id, Member member, Project project, LikeStatus status) {
        this.id = new ProjectLikeDislikeId(member.getId(), project.getId());
        this.member = member;
        this.project = project;
        this.status = status;
    }
}
