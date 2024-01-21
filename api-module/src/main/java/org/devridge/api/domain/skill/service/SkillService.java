package org.devridge.api.domain.skill.service;

import lombok.RequiredArgsConstructor;
import org.devridge.api.domain.skill.dto.response.SkillInformation;
import org.devridge.api.domain.skill.entity.Skill;
import org.devridge.api.domain.skill.mapper.SkillMapper;
import org.devridge.api.domain.skill.repository.SkillRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class SkillService {

    private final SkillRepository skillRepository;
    private final SkillMapper skillMapper;

    public List<SkillInformation> getAllSkills() {
        List<Skill> skills = skillRepository.findAll();
        List<SkillInformation> skillInformations = new ArrayList<>();

        skills.forEach(
                skill -> skillInformations.add(
                        skillMapper.toSkillInformation(skill)
                )
        );
        
        return skillInformations;
    }
}
