package org.devridge.api.domain.community.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SkillResponse {
    private Long id;
    private String skill;
}
