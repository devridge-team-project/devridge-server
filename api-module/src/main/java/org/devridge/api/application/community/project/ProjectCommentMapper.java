package org.devridge.api.application.community.project;

import lombok.RequiredArgsConstructor;
import org.devridge.api.domain.community.dto.request.ProjectCommentRequest;
import org.devridge.api.domain.community.entity.Project;
import org.devridge.api.domain.community.entity.ProjectComment;
import org.devridge.api.domain.member.entity.Member;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProjectCommentMapper {

    public ProjectComment toProjectComment(Project project, Member member, ProjectCommentRequest Request) {
        return ProjectComment.builder()
            .project(project)
            .content(Request.getContent())
            .member(member)
            .build();
    }

}
