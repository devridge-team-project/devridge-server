package org.devridge.api.domain.community.service;

import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.devridge.api.domain.community.dto.request.ProjectRequest;
import org.devridge.api.domain.community.dto.response.ProjectDetailResponse;
import org.devridge.api.domain.community.dto.response.ProjectListResponse;
import org.devridge.api.domain.community.entity.Project;
import org.devridge.api.domain.community.exception.MyCommunityForbiddenException;
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

    @Transactional
    public ProjectDetailResponse getProjectDetail(Long projectId) {
        Project project = getProjectById(projectId);
        projectRepository.updateView(projectId);
        return projectMapper.toProjectDetailResponse(project);
    }

    public List<ProjectListResponse> getAllProject() {
        List<Project> project = projectRepository.findAll();
        return projectMapper.toProjectListResponses(project);
    }

    @Transactional
    public void updateProject(Long projectId, ProjectRequest request) {
        Long accessMemberId = SecurityContextHolderUtil.getMemberId();
        getMemberById(accessMemberId);
        Project project = getProjectById(projectId);

        if (!accessMemberId.equals(project.getMember().getId())) {
            throw new MyCommunityForbiddenException(403, "내가 작성하지 않은 글은 수정할 수 없습니다.");
        }

        if (request.getImages() != null && !request.getImages().isEmpty()) {
            String images = request.getImages().toString();
            project.updateProject(request.getTitle(), request.getContent(), request.getCategory().getValue(), images.substring(1, images.length() - 1));
            return;
        }
        project.updateProject(request.getTitle(), request.getContent(), request.getCategory().getValue(), null);
    }

    @Transactional
    public void deleteProject(Long projectId) {
        Long accessMemberId = SecurityContextHolderUtil.getMemberId();
        Project project = getProjectById(projectId);

        if (!accessMemberId.equals(project.getMember().getId())) {
            throw new MyCommunityForbiddenException(403, "내가 작성하지 않은 글은 삭제할 수 없습니다.");
        }

        projectRepository.delete(project);

        if (project.getImages() != null) {
            List<String> images = Arrays.asList(project.getImages().split(", "));
            s3Service.deleteAllImage(images);
        }
    }

    public Project getProjectById(Long projectId) {
        return projectRepository.findById(projectId).orElseThrow(() -> new DataNotFoundException());
    }

    private Member getMemberById(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(() -> new DataNotFoundException());
    }
}
