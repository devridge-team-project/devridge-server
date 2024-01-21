package org.devridge.api.domain.skill.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devridge.api.domain.AbstractTimeEntity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "skill")
public class Skill extends AbstractTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String skill;

    @Column(name = "is_deleted", columnDefinition = "TINYINT(1)")
    private boolean isDeleted;

    @OneToMany(mappedBy = "skill")
    private Set<MemberSkill> memberSkills = new HashSet<>();

    @Builder
    public Skill(String skill, boolean isDeleted, Set<MemberSkill> memberSkills) {
        this.skill = skill;
        this.isDeleted = isDeleted;
        this.memberSkills = memberSkills;
    }
}
