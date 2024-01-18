package org.devridge.api.domain.community.repository;

import org.devridge.api.domain.community.entity.CommunityCommentLikeDislike;
import org.devridge.api.domain.community.entity.id.CommunityCommentLikeDislikeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommunityCommentLikeDislikeRepository extends
    JpaRepository<CommunityCommentLikeDislike, CommunityCommentLikeDislikeId> {

}
