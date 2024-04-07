package org.devridge.api.application.community.project;

import org.devridge.api.domain.community.entity.Project;
import org.devridge.api.domain.community.entity.ProjectScrap;
import org.devridge.api.domain.member.entity.Member;
import org.springframework.stereotype.Component;

@Component
public class ProjectScrapMapper {

    public ProjectScrap toProjectScrap(Project project, Member member) {
        return ProjectScrap.builder()
            .project(project)
            .member(member)
            .build();
    }
}