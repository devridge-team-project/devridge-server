package org.devridge.api.domain.skill.repository;

import org.devridge.api.domain.skill.entity.ProjectSkill;
import org.devridge.api.domain.skill.entity.key.ProjectSkillId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProjectSkillRepository extends JpaRepository<ProjectSkill, ProjectSkillId> {

    @Modifying
    @Query(
        value = "DELETE FROM ProjectSkill p " +
                "WHERE p.id.projectId = :id"
    )
    void deleteByProjectId(@Param("id") Long id);
}
