package org.devridge.api.infrastructure.community.project;

import org.devridge.api.domain.community.entity.ProjectScrap;
import org.devridge.api.domain.community.entity.id.ProjectScrapId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProjectScrapRepository extends JpaRepository<ProjectScrap, ProjectScrapId> {

    @Modifying
    @Query(
        value = "UPDATE ProjectScrap ps " +
            "SET ps.isDeleted = false " +
            "WHERE ps.id = :id"
    )
    void restoreById(@Param("id") ProjectScrapId id);
}