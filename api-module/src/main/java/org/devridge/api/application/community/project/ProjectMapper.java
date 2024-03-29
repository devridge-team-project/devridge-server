package org.devridge.api.application.community.project;


import java.util.ArrayList;
import java.util.List;

import lombok.RequiredArgsConstructor;

import org.devridge.api.common.dto.UserInformation;
import org.devridge.api.domain.community.dto.request.ProjectRequest;
import org.devridge.api.domain.community.dto.response.ProjectDetailResponse;
import org.devridge.api.domain.community.dto.response.ProjectListResponse;
import org.devridge.api.domain.community.entity.Project;
import org.devridge.api.domain.member.entity.Member;

import org.springframework.stereotype.Component;

import static org.devridge.api.common.util.MemberUtil.toMember;

@Component
@RequiredArgsConstructor
public class ProjectMapper {

    public ProjectDetailResponse toProjectDetailResponse(Project project, List<String> skills) {
        Member member = project.getMember();

        UserInformation userInformation = toMember(member);

        return ProjectDetailResponse.builder()
                .communityId(project.getId())
                .userInformation(userInformation)
                .title(project.getTitle())
                .content(project.getContent())
                .likes(project.getLikes())
                .dislikes(project.getDislikes())
                .views(project.getViews() + 1)
                .createdAt(project.getCreatedAt())
                .updatedAt(project.getUpdatedAt())
                .category(project.getCategory())
                .meeting(project.getMeeting())
                .skills(skills)
                .isRecruiting(project.getIsRecruiting())
                .build();
    }

    public Project toProject(ProjectRequest request, Member member) {
        if (request.getImages() != null && !request.getImages().isEmpty()) {
            String images = request.getImages().toString();
            return Project.builder()
                    .member(member)
                    .title(request.getTitle())
                    .content(request.getContent())
                    .images(images.substring(1, images.length() -1))
                    .category(request.getCategory())
                    .meeting(request.getMeeting())
                    .build();
        }

        return Project.builder()
                .member(member)
                .title(request.getTitle())
                .content(request.getContent())
                .category(request.getCategory())
                .meeting(request.getMeeting())
                .build();
    }

    public ProjectListResponse toProjectListResponse(Project project) {
        return ProjectListResponse.builder()
            .id(project.getId())
            .views(project.getViews())
            .category(project.getCategory())
            .likes(project.getLikes())
            .dislikes(project.getDislikes())
            .title(project.getTitle())
            .content(project.getContent())
            .build();
    }
}
