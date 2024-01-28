package org.devridge.api.domain.community.repository;

import org.devridge.api.domain.community.entity.CommunityLikeDislike;
import org.devridge.api.domain.community.entity.LikeStatus;
import org.devridge.api.domain.community.entity.id.CommunityLikeDislikeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommunityLikeDislikeRepository extends
    JpaRepository<CommunityLikeDislike, CommunityLikeDislikeId> {

    @Modifying
    @Query(
        value = "UPDATE CommunityLikeDislike cl " +
                "SET cl.status = :status " +
                "WHERE cl.id = :id"
    )
    void updateLikeDislike(@Param("id") CommunityLikeDislikeId id, @Param("status") LikeStatus status);

    @Modifying
    @Query(
        value = "UPDATE CommunityLikeDislike cl " +
                "SET cl.isDeleted = false " +
                "WHERE cl.id = :id"
    )
    void restoreById(@Param("id") CommunityLikeDislikeId id);
}
