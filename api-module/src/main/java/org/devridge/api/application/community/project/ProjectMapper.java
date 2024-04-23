package org.devridge.api.application.community.project;


import static org.devridge.api.common.util.MemberUtil.toMember;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.devridge.api.common.dto.UserInformation;
import org.devridge.api.domain.community.dto.request.ProjectRequest;
import org.devridge.api.domain.community.dto.response.ProjectDetailResponse;
import org.devridge.api.domain.community.dto.response.ProjectListResponse;
import org.devridge.api.domain.community.entity.Project;
import org.devridge.api.domain.member.entity.Member;
import org.springframework.stereotype.Component;

@Component
public class ProjectMapper {

    public ProjectDetailResponse toProjectDetailResponse(Project project, List<String> skills) {
        Member member = project.getMember();

        UserInformation userInformation = toMember(member);

        String roles = project.getRoles();
        List<String> rolesList = Arrays.asList(roles.split("\\s*,\\s*"));

        return ProjectDetailResponse.builder()
                .communityId(project.getId())
                .userInformation(userInformation)
                .title(project.getTitle())
                .content(project.getContent())
                .likes(project.getLikes())
                .dislikes(project.getDislikes())
                .comments(Long.valueOf(project.getComments().size()))
                .scraps(Long.valueOf(project.getScraps().size()))
                .views(project.getViews() + 1)
                .createdAt(project.getCreatedAt())
                .updatedAt(project.getUpdatedAt())
                .roles(rolesList)
                .meeting(project.getMeeting())
                .skills(skills)
                .isRecruiting(project.getIsRecruiting())
                .build();
    }

    public Project toProject(ProjectRequest request, Member member) {
        Project.ProjectBuilder builder = Project.builder()
                .member(member)
                .title(request.getTitle())
                .content(request.getContent())
                .meeting(request.getMeeting());

        if (request.getRoles() != null && !request.getRoles().isEmpty()) {
            String roles = request.getRoles().toString();
            builder.roles(roles.substring(1, roles.length() - 1));
        }

        if (request.getImages() != null && !request.getImages().isEmpty()) {
            String images = request.getImages().toString();
            builder.images(images.substring(1, images.length() -1));
        }

        return builder.build();
    }

    public List<ProjectListResponse> toProjectListResponses(List<Project> projects) {
        List<ProjectListResponse> projectListResponses = new ArrayList<>();

        for (Project project : projects) {
            projectListResponses.add(toProjectListResponse(project));
        }

        return projectListResponses;
    }

    public ProjectListResponse toProjectListResponse(Project project) {
        String roles = project.getRoles();
        List<String> rolesList = Arrays.asList(roles.split("\\s*,\\s*"));

        return ProjectListResponse.builder()
                .id(project.getId())
                .title(project.getTitle())
                .content(project.getContent())
                .likes(project.getLikes())
                .dislikes(project.getDislikes())
                .views(project.getViews())
                .isRecruiting(project.getIsRecruiting())
                .meeting(project.getMeeting())
                .roles(rolesList)
                .build();
    }
}
