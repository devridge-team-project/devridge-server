package org.devridge.api.domain.community.mapper;


import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.devridge.api.domain.community.dto.request.ProjectRequest;
import org.devridge.api.domain.community.dto.response.MemberInfoResponse;
import org.devridge.api.domain.community.dto.response.ProjectDetailResponse;
import org.devridge.api.domain.community.dto.response.ProjectListResponse;
import org.devridge.api.domain.community.entity.Project;
import org.devridge.api.domain.member.entity.Member;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProjectMapper {

    private final MemberInfoMapper memberInfoMapper;

    public ProjectDetailResponse toProjectDetailResponse(Project project, List<String> skills) {
        Member member = project.getMember();
        MemberInfoResponse memberInfoResponse = memberInfoMapper.toMemberInfoResponse(member);
        return ProjectDetailResponse.builder()
                .communityId(project.getId())
                .memberInfoResponse(memberInfoResponse)
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
                    .category(request.getCategory().getValue())
                    .meeting(request.getMeeting().toString())
                    .build();
        }
        return Project.builder()
                .member(member)
                .title(request.getTitle())
                .content(request.getContent())
                .category(request.getCategory().getValue())
                .meeting(request.getMeeting().toString())
                .build();
    }

    public ProjectListResponse toProjectListResponse(Project project) {
        return ProjectListResponse.builder()
            .id(project.getId())
            .view(project.getViews())
            .category(project.getCategory())
            .likes(project.getLikes())
            .dislikes(project.getDislikes())
            .title(project.getTitle())
            .content(project.getContent())
            .build();
    }

    public List<ProjectListResponse> toProjectListResponses(List<Project> projects) {
        List<ProjectListResponse> projectListResponses = new ArrayList<>();
        for (Project project : projects) {
            projectListResponses.add(
                toProjectListResponse(project)
            );
        }
        return projectListResponses;
    }
}
