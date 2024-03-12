package org.devridge.api.domain.skill.entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devridge.api.domain.community.entity.Project;
import org.devridge.api.domain.skill.entity.key.ProjectSkillId;
import org.devridge.api.common.entity.BaseTimeEntity;
import org.hibernate.annotations.DynamicInsert;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
public class ProjectSkill extends BaseTimeEntity {

    @EmbeddedId
    private ProjectSkillId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("projectId")
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("skillId")
    @JoinColumn(name = "skill_id")
    private Skill skill;

    @Builder
    public ProjectSkill(Project project, Skill skill) {
        this.id = new ProjectSkillId(project.getId(), skill.getId());
        this.project = project;
        this.skill = skill;
    }
}
