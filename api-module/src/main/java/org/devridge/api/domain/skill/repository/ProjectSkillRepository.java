package org.devridge.api.domain.skill.repository;

import org.devridge.api.domain.skill.entity.ProjectSkill;
import org.devridge.api.domain.skill.entity.key.ProjectSkillId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectSkillRepository extends JpaRepository<ProjectSkill, ProjectSkillId> {

}
