package org.devridge.api.infrastructure.community.project;

import org.devridge.api.domain.community.entity.LikeStatus;
import org.devridge.api.domain.community.entity.ProjectLikeDislike;
import org.devridge.api.domain.community.entity.id.ProjectLikeDislikeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProjectLikeDislikeRepository extends JpaRepository<ProjectLikeDislike, ProjectLikeDislikeId> {

    @Modifying
    @Query(
        value = "UPDATE ProjectLikeDislike pl " +
            "SET pl.status = :status " +
            "WHERE pl.id = :id"
    )
    void updateLikeDislike(@Param("id") ProjectLikeDislikeId id, @Param("status") LikeStatus status);

    @Modifying
    @Query(
        value = "UPDATE ProjectLikeDislike pl " +
            "SET pl.isDeleted = false " +
            "WHERE pl.id = :id"
    )
    void restoreById(@Param("id") ProjectLikeDislikeId id);

    @Query(
        value = "SELECT COUNT(pl) " +
            "FROM ProjectLikeDislike pl " +
            "WHERE pl.id.projectId = :projectId AND pl.status = :status AND pl.isDeleted = false"
    )
    int countProjectLikeDislikeByProjectId(@Param("projectId") Long projectId, @Param("status") LikeStatus status);
}
