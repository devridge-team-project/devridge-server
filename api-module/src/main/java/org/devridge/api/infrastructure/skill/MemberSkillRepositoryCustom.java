package org.devridge.api.infrastructure.skill;

import org.devridge.api.domain.skill.entity.MemberSkill;

import java.util.List;

public interface MemberSkillRepositoryCustom {

    void bulkInsert(List<MemberSkill> memberSkills);
}
