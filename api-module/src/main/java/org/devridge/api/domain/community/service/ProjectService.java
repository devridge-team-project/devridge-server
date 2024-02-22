package org.devridge.api.domain.community.service;

import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.devridge.api.domain.community.dto.request.ProjectRequest;
import org.devridge.api.domain.community.dto.response.ProjectListResponse;
import org.devridge.api.domain.community.entity.Project;
import org.devridge.api.domain.community.mapper.ProjectMapper;
import org.devridge.api.domain.community.repository.ProjectRepository;
import org.devridge.api.domain.member.entity.Member;
import org.devridge.api.domain.member.repository.MemberRepository;
import org.devridge.api.domain.s3.service.S3Service;
import org.devridge.api.exception.common.DataNotFoundException;
import org.devridge.api.util.SecurityContextHolderUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;
    private final MemberRepository memberRepository;
    private final S3Service s3Service;

    public Long createProject(ProjectRequest request) {
        Long accessMemberId = SecurityContextHolderUtil.getMemberId();
        Member member = getMemberById(accessMemberId);

        Project project = projectMapper.toProject(request, member);
        return projectRepository.save(project).getId();
    }

    public List<ProjectListResponse> getAllProject() {
        List<Project> project = projectRepository.findAll();
        return projectMapper.toProjectListResponses(project);
    }

    @Transactional
    public void updateProject(Long projectId, ProjectRequest request) {
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new DataNotFoundException());

        if (request.getImages() != null && !request.getImages().isEmpty()) {
            String images = request.getImages().toString();
            project.updateProject(request.getTitle(), request.getContent(), request.getCategory().getValue(), images.substring(1, images.length() - 1));
            return;
        }
        project.updateProject(request.getTitle(), request.getContent(), request.getCategory().getValue(), null);
    }

    public void deleteProject(Long projectId) {
        projectRepository.findById(projectId).ifPresentOrElse(
            project ->{
                projectRepository.delete(project);
                if (project.getImages() != null) {
                    List<String> images = Arrays.asList(project.getImages().split(", "));
                    s3Service.deleteAllImage(images);
                }
            },
            () -> {
                throw new DataNotFoundException();
            }
        );

    }

    private Member getMemberById(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(() -> new DataNotFoundException());
    }
}
