package org.devridge.api.domain.communitycommentlikedislike;

import java.io.Serializable;
import javax.persistence.Embeddable;

@Embeddable
public class CommunityCommentLikeDislikeId implements Serializable {
    private Long memberId;
    private Long commentId;
}
