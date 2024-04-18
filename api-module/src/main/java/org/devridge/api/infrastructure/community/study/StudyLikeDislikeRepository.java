package org.devridge.api.infrastructure.community.study;

import org.devridge.api.domain.community.entity.LikeStatus;
import org.devridge.api.domain.community.entity.StudyLikeDislike;
import org.devridge.api.domain.community.entity.id.StudyLikeDislikeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StudyLikeDislikeRepository extends JpaRepository<StudyLikeDislike, StudyLikeDislikeId> {

    @Modifying
    @Query(
        value = "UPDATE StudyLikeDislike pl " +
            "SET pl.status = :status " +
            "WHERE pl.id = :id"
    )
    void updateLikeDislike(@Param("id") StudyLikeDislikeId id, @Param("status") LikeStatus status);

    @Modifying
    @Query(
        value = "UPDATE StudyLikeDislike pl " +
            "SET pl.isDeleted = false " +
            "WHERE pl.id = :id"
    )
    void restoreById(@Param("id") StudyLikeDislikeId id);

    @Query(
        value = "SELECT COUNT(pl) " +
                "FROM StudyLikeDislike pl " +
                "WHERE pl.id.studyId = :studyId AND pl.status = :status AND pl.isDeleted = false"
    )
    int countStudyLikeDislikeByStudyId(@Param("studyId") Long studyId, @Param("status") LikeStatus status);
}
