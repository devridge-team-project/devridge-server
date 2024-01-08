package org.devridge.api.domain.communityscrap;

import java.time.LocalDateTime;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import lombok.Getter;
import org.devridge.api.domain.community.Community;
import org.devridge.api.githubsociallogintemp.domain.Member;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
@Getter
@Entity
public class CommunityScrap {
    @EmbeddedId
    private CommunityScrapId id;

    @MapsId("memberId")
    @ManyToOne
    @JoinColumn(name = "member_id", insertable = false, updatable = false)
    private Member member;

    @MapsId("communityId")
    @ManyToOne
    @JoinColumn(name = "community_id", insertable = false, updatable = false)
    private Community community;



    private Boolean isDeleted;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
