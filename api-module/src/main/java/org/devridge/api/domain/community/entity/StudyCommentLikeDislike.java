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
import org.devridge.api.domain.community.entity.id.StudyCommentLikeDislikeId;
import org.devridge.api.domain.member.entity.Member;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.SQLDelete;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@DynamicInsert
@SQLDelete(sql = "UPDATE study_comment_like_dislike SET is_deleted = true WHERE study_comment_id = ? AND member_id = ?")
public class StudyCommentLikeDislike extends BaseTimeEntity {

    @EmbeddedId
    private StudyCommentLikeDislikeId id;

    @MapsId("memberId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", insertable = false, updatable = false)
    private Member member;

    @MapsId("commentId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_comment_id", insertable = false, updatable = false)
    private StudyComment studyComment;

    @Enumerated(EnumType.STRING)
    private LikeStatus status;

    @Builder
    public StudyCommentLikeDislike(StudyCommentLikeDislikeId id, Member member,
        StudyComment studyComment, LikeStatus status) {
        this.id = new StudyCommentLikeDislikeId(member.getId(), studyComment.getId());
        this.member = member;
        this.studyComment = studyComment;
        this.status = status;
    }
}