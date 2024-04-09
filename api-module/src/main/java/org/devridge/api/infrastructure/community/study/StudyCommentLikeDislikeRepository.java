package org.devridge.api.infrastructure.community.study;

import org.devridge.api.domain.community.entity.LikeStatus;
import org.devridge.api.domain.community.entity.StudyCommentLikeDislike;
import org.devridge.api.domain.community.entity.id.StudyCommentLikeDislikeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StudyCommentLikeDislikeRepository extends
    JpaRepository<StudyCommentLikeDislike, StudyCommentLikeDislikeId> {

    @Modifying
    @Query(
        value = "UPDATE StudyCommentLikeDislike pl " +
                "SET pl.status = :status " +
                "WHERE pl.id = :id"
    )
    void updateLikeDislike(@Param("id") StudyCommentLikeDislikeId id, @Param("status") LikeStatus status);

    @Modifying
    @Query(
        value = "UPDATE StudyCommentLikeDislike pl " +
                "SET pl.isDeleted = false " +
                "WHERE pl.id = :id"
    )
    void restoreById(@Param("id") StudyCommentLikeDislikeId id);

    @Query(
        value = "SELECT COUNT(pld) " +
                "FROM StudyCommentLikeDislike pld " +
                "WHERE pld.id.commentId = :commentId AND pld.status = :status AND pld.isDeleted = false"
    )
    int countStudyLikeDislikeById(@Param("commentId") Long commentId, @Param("status") LikeStatus status);
}
