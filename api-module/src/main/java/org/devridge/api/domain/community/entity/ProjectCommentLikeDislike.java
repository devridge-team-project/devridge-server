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
import org.devridge.api.domain.community.entity.id.ProjectCommentLikeDislikeId;
import org.devridge.api.domain.member.entity.Member;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.SQLDelete;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@DynamicInsert
@SQLDelete(sql = "UPDATE project_comment_like_dislike SET is_deleted = true WHERE project_comment_id = ? AND member_id = ?")
public class ProjectCommentLikeDislike extends BaseTimeEntity {

    @EmbeddedId
    private ProjectCommentLikeDislikeId id;

    @MapsId("memberId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", insertable = false, updatable = false)
    private Member member;

    @MapsId("commentId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_comment_id", insertable = false, updatable = false)
    private ProjectComment projectComment;

    @Enumerated(EnumType.STRING)
    private LikeStatus status;

    @Builder
    public ProjectCommentLikeDislike(ProjectCommentLikeDislikeId id, Member member,
        ProjectComment projectComment, LikeStatus status) {
        this.id = new ProjectCommentLikeDislikeId(member.getId(), projectComment.getId());
        this.member = member;
        this.projectComment = projectComment;
        this.status = status;
    }
}
