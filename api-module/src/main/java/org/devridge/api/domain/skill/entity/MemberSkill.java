package org.devridge.api.domain.skill.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devridge.api.domain.member.entity.Member;
import org.devridge.api.domain.skill.entity.key.MemberSkillId;
import org.devridge.common.dto.BaseTimeEntity;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "member_skill")
public class MemberSkill extends BaseTimeEntity {

    @EmbeddedId
    private MemberSkillId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("memberId") // MemberSkillId 내의 memberId와 매핑
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @MapsId("skillId") // MemberSkillId 내의 skillId와 매핑
    @JoinColumn(name = "skill_id")
    private Skill skill;

    @Builder
    public MemberSkill(MemberSkillId id, Member member, Skill skill) {
        this.id = id;
        this.member = member;
        this.skill = skill;
    }
}
