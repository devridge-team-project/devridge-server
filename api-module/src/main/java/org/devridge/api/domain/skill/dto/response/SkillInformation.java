package org.devridge.api.domain.skill.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class SkillInformation {

    private Long id;
    private String skillName;
}
