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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devridge.api.domain.AbstractTimeEntity;
import org.devridge.api.domain.community.entity.id.CommunityCommentLikeDislikeId;
import org.devridge.api.domain.member.entity.Member;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@DynamicInsert
@SQLDelete(sql = "UPDATE community_comment_like_dislike SET is_deleted = true WHERE community_comment_id = ? AND member_id = ?")
@Where(clause = "is_deleted = false")
public class CommunityCommentLikeDislike extends AbstractTimeEntity {

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

    private Boolean isDeleted;

    public void changeStatus(LikeStatus status) {
        this.status = status;
    }
}
