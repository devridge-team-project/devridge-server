package org.devridge.api.domain.skill.entity.key;

import java.io.Serializable;
import javax.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ProjectSkillId implements Serializable {

    private Long projectId;
    private Long skillId;
}
