package org.devridge.api.domain.skill.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devridge.api.domain.member.entity.Member;
import org.devridge.api.domain.skill.entity.key.MemberSkillId;
import org.devridge.api.common.entity.BaseTimeEntity;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
@Table(name = "member_skill")
public class MemberSkill extends BaseTimeEntity {

    @EmbeddedId
    private MemberSkillId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("memberId")
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @MapsId("skillId")
    @JoinColumn(name = "skill_id")
    private Skill skill;

    @Builder
    public MemberSkill(MemberSkillId id, Member member, Skill skill) {
        this.id = id;
        this.member = member;
        this.skill = skill;
    }
}
