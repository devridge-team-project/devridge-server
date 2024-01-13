package org.devridge.api.domain.communitycommentlikedislike;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommunityCommentLikeDislikeRepository extends
    JpaRepository<CommunityCommentLikeDislike, CommunityCommentLikeDislikeId> {

}
