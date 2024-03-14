package org.devridge.api.infrastructure.skill;

import org.devridge.api.domain.skill.entity.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SkillRepository extends JpaRepository<Skill, Long> {

    @Query("SELECT s FROM Skill s WHERE s.id IN :ids")
    List<Skill> findSkillsByIds(@Param("ids") List<Long> ids);
}
