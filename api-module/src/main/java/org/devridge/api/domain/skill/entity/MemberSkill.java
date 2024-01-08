package org.devridge.api.domain.skill.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devridge.api.domain.AbstractTimeEntity;
import org.devridge.api.domain.member.entity.Member;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "member_skill")
public class MemberSkill extends AbstractTimeEntity {

    @EmbeddedId
    private MemberSkillId id;

    @ManyToOne
    @MapsId("memberId") // MemberSkillId 내의 memberId와 매핑
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @MapsId("skillId") // MemberSkillId 내의 skillId와 매핑
    @JoinColumn(name = "skill_id")
    private Skill skill;

    private boolean isDeleted;

}