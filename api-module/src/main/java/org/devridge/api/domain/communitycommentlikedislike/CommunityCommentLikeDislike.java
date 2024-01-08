package org.devridge.api.domain.communitycommentlikedislike;

import java.time.LocalDateTime;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import lombok.Getter;
import org.devridge.api.domain.communitycomment.CommunityComment;
import org.devridge.api.githubsociallogintemp.domain.Member;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

@Getter
@Entity
public class CommunityCommentLikeDislike {
    @EmbeddedId
    private CommunityCommentLikeDislikeId id;

    @MapsId("memberId")
    @ManyToOne
    @JoinColumn(name = "member_id", insertable = false, updatable = false)
    private Member member;

    @MapsId("commentId")
    @ManyToOne
    @JoinColumn(name = "comment_id", insertable = false, updatable = false)
    private CommunityComment communityComment;

    private Character status;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    private Boolean isDeleted;
}
