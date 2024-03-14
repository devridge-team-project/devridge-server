package org.devridge.api.domain.community.entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devridge.api.domain.community.entity.id.CommunityScrapId;
import org.devridge.api.domain.member.entity.Member;
import org.devridge.api.common.entity.BaseTimeEntity;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.SQLDelete;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@DynamicInsert
@SQLDelete(sql = "UPDATE community_scrap SET is_deleted = true WHERE community_id = ? AND member_id = ?")
public class CommunityScrap extends BaseTimeEntity {

    @EmbeddedId
    private CommunityScrapId id;

    @MapsId("memberId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", insertable = false, updatable = false)
    private Member member;

    @MapsId("communityId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "community_id", insertable = false, updatable = false)
    private Community community;

    @Builder
    public CommunityScrap(CommunityScrapId id, Member member, Community community) {
        this.id = new CommunityScrapId(member.getId(), community.getId());
        this.member = member;
        this.community = community;
    }
}
