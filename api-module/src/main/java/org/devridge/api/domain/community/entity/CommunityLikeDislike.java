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
import org.devridge.api.domain.community.entity.id.CommunityLikeDislikeId;
import org.devridge.api.domain.member.entity.Member;
import org.devridge.common.entity.BaseTimeEntity;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.SQLDelete;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@DynamicInsert
@SQLDelete(sql = "UPDATE community_like_dislike SET is_deleted = true WHERE community_id = ? AND member_id = ?")
public class CommunityLikeDislike extends BaseTimeEntity {

    @EmbeddedId
    private CommunityLikeDislikeId id;

    @MapsId("memberId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", insertable = false, updatable = false)
    private Member member;


    @MapsId("communityId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "community_id", insertable = false, updatable = false)
    private Community community;

    @Enumerated(EnumType.STRING)
    private LikeStatus status;

    @Builder
    public CommunityLikeDislike(CommunityLikeDislikeId id, Member member, Community community, LikeStatus status) {
        this.id = new CommunityLikeDislikeId(member.getId(), community.getId());
        this.member = member;
        this.community = community;
        this.status = status;
    }

    public void updateStatus(LikeStatus status) {
        this.status = status;
    }
}
