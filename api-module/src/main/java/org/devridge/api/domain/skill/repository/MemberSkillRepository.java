package org.devridge.api.domain.skill.repository;

import org.devridge.api.domain.skill.entity.MemberSkill;
import org.devridge.api.domain.skill.entity.MemberSkillId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberSkillRepository extends JpaRepository<MemberSkill, MemberSkillId> {

}
