package org.devridge.api.domain.skill.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devridge.api.domain.AbstractTimeEntity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
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

}
