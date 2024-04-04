package org.devridge.api.infrastructure.community.project;

import org.devridge.api.domain.community.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    @Modifying
    @Query(
        value = "UPDATE Project p " +
            "SET p.views = p.views + 1 " +
            "WHERE p.id = :id")
    void updateView(@Param("id") Long id);

    @Modifying
    @Query(
        value = "UPDATE Project " +
            "SET likes = :likes, dislikes = :dislikes " +
            "WHERE id = :id"
    )
    void updateLikeDislike(
        @Param("likes") Long likes,
        @Param("dislikes") Long dislikes,
        @Param("id") Long id
    );
}
