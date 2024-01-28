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
import org.devridge.api.domain.community.entity.id.CommunityCommentLikeDislikeId;
import org.devridge.api.domain.member.entity.Member;
import org.devridge.common.entity.BaseTimeEntity;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@DynamicInsert
@SQLDelete(sql = "UPDATE community_comment_like_dislike SET is_deleted = true WHERE community_comment_id = ? AND member_id = ?")
@Where(clause = "is_deleted = false")
public class CommunityCommentLikeDislike extends BaseTimeEntity {

    @EmbeddedId
    private CommunityCommentLikeDislikeId id;

    @MapsId("memberId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", insertable = false, updatable = false)
    private Member member;

    @MapsId("commentId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "community_comment_id", insertable = false, updatable = false)
    private CommunityComment communityComment;

    @Enumerated(EnumType.STRING)
    private LikeStatus status;

    @Builder
    public CommunityCommentLikeDislike(CommunityCommentLikeDislikeId id, Member member,
        CommunityComment communityComment, LikeStatus status) {
        this.id = new CommunityCommentLikeDislikeId(member.getId(), communityComment.getId());
        this.member = member;
        this.communityComment = communityComment;
        this.status = status;
    }

    public void changeStatus(LikeStatus status) {
        this.status = status;
    }
}
