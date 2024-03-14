package org.devridge.api.domain.community.entity;


import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devridge.api.domain.community.entity.id.CommunityHashtagId;
import org.devridge.api.common.entity.BaseTimeEntity;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Getter
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE community_hashtag SET is_deleted = true WHERE community_id = ? AND hashtag_id = ?")
@Where(clause = "is_deleted = false")
public class CommunityHashtag extends BaseTimeEntity {

    @EmbeddedId
    private CommunityHashtagId id;

    @MapsId("communityId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "community_id")
    private Community community;

    @MapsId("hashtagId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hashtag_id")
    private Hashtag hashtag;

    public CommunityHashtag(Community community, Hashtag hashtag) {
        this.id = new CommunityHashtagId(community.getId(), hashtag.getId());
        this.community = community;
        this.hashtag = hashtag;
    }

}
