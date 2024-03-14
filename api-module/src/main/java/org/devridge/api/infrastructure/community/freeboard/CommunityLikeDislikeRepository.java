package org.devridge.api.infrastructure.community.freeboard;

import org.devridge.api.domain.community.entity.CommunityLikeDislike;
import org.devridge.api.domain.community.entity.LikeStatus;
import org.devridge.api.domain.community.entity.id.CommunityLikeDislikeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommunityLikeDislikeRepository extends JpaRepository<CommunityLikeDislike, CommunityLikeDislikeId> {

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

    @Query(
        value = "SELECT COUNT(cld) " +
                "FROM CommunityLikeDislike cld " +
                "WHERE cld.id.communityId = :communityId AND cld.status = :status AND cld.isDeleted = false"
    )
    int countCommunityLikeDislikeByCommunityId(@Param("communityId") Long communityId, @Param("status") LikeStatus status);
}
