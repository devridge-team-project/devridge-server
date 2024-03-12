package org.devridge.api.application.skill;

import org.devridge.api.domain.skill.dto.response.SkillInformation;
import org.devridge.api.domain.skill.entity.Skill;
import org.springframework.stereotype.Component;

@Component
public class SkillMapper {

    public SkillInformation toSkillInformation(Skill skill) {
        return new SkillInformation(skill.getId(), skill.getSkill());
    }
}
