package org.devridge.api.domain.skill.entity.key;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class MemberSkillId implements Serializable {

    private Long memberId;
    private Long skillId;

}
