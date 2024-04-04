package org.devridge.api.infrastructure.community.project;

import org.devridge.api.domain.community.entity.ProjectComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProjectCommentRepository extends JpaRepository<ProjectComment, Long> {

    @Modifying
    @Query(
        value = "UPDATE ProjectComment " +
                "SET likes = :likes, dislikes = :dislikes " +
                "WHERE id = :id")
    void updateLikeDislike(@Param("likes") Long likes, @Param("dislikes") Long dislikes, @Param("id") Long id);
}
