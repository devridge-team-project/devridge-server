package org.devridge.api.application.community.project;

import org.devridge.api.domain.community.entity.LikeStatus;
import org.devridge.api.domain.community.entity.Project;
import org.devridge.api.domain.community.entity.ProjectLikeDislike;
import org.devridge.api.domain.member.entity.Member;
import org.springframework.stereotype.Component;

@Component
public class ProjectLikeDislikeMapper {

    public ProjectLikeDislike toProjectLikeDislike(Project project, Member member, LikeStatus status) {
        return ProjectLikeDislike.builder()
            .project(project)
            .member(member)
            .status(status)
            .build();
    }
}
