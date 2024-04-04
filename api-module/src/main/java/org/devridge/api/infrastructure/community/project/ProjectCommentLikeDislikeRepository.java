package org.devridge.api.infrastructure.community.project;

import org.devridge.api.domain.community.entity.LikeStatus;
import org.devridge.api.domain.community.entity.ProjectCommentLikeDislike;
import org.devridge.api.domain.community.entity.id.ProjectCommentLikeDislikeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProjectCommentLikeDislikeRepository extends
    JpaRepository<ProjectCommentLikeDislike, ProjectCommentLikeDislikeId> {

    @Modifying
    @Query(
        value = "UPDATE ProjectCommentLikeDislike pl " +
                "SET pl.status = :status " +
                "WHERE pl.id = :id"
    )
    void updateLikeDislike(@Param("id") ProjectCommentLikeDislikeId id, @Param("status") LikeStatus status);

    @Modifying
    @Query(
        value = "UPDATE ProjectCommentLikeDislike pl " +
                "SET pl.isDeleted = false " +
                "WHERE pl.id = :id"
    )
    void restoreById(@Param("id") ProjectCommentLikeDislikeId id);

    @Query(
        value = "SELECT COUNT(pld) " +
                "FROM ProjectCommentLikeDislike pld " +
                "WHERE pld.id.commentId = :commentId AND pld.status = :status AND pld.isDeleted = false"
    )
    int countProjectLikeDislikeById(@Param("commentId") Long commentId, @Param("status") LikeStatus status);
}
