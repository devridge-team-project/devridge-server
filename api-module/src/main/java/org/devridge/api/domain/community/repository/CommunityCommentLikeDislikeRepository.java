package org.devridge.api.domain.community.repository;

import org.devridge.api.domain.community.entity.CommunityCommentLikeDislike;
import org.devridge.api.domain.community.entity.LikeStatus;
import org.devridge.api.domain.community.entity.id.CommunityCommentLikeDislikeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CommunityCommentLikeDislikeRepository extends
    JpaRepository<CommunityCommentLikeDislike, CommunityCommentLikeDislikeId> {

    @Modifying
    @Query(
        value = "UPDATE CommunityCommentLikeDislike cl " +
                "SET cl.status = :status " +
                "WHERE cl.id = :id"
    )
    void updateLikeDislike(@Param("id") CommunityCommentLikeDislikeId id, @Param("status") LikeStatus status);

    @Modifying
    @Query(
        value = "UPDATE CommunityCommentLikeDislike cl " +
                "SET cl.isDeleted = false " +
                "WHERE cl.id = :id"
    )
    void restoreById(@Param("id") CommunityCommentLikeDislikeId id);

    @Query(
        value = "SELECT COUNT(cld) " +
            "FROM CommunityCommentLikeDislike cld " +
            "WHERE cld.id = :id AND cld.status = :status AND cld.isDeleted = false"
    )
    int countCommunityLikeDislikeById(@Param("id") CommunityCommentLikeDislikeId id, @Param("status") LikeStatus status);
}
