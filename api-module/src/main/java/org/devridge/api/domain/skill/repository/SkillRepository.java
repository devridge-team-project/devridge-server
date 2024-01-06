package org.devridge.api.domain.skill.repository;

import org.devridge.api.domain.skill.entity.Skill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SkillRepository extends JpaRepository<Skill, Long> {

    public Optional<Skill> findBySkill(String skill);
}
