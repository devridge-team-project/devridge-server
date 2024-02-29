package org.devridge.api.domain.community.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.devridge.api.domain.community.dto.request.ProjectRequest;
import org.devridge.api.domain.community.dto.response.ProjectDetailResponse;
import org.devridge.api.domain.community.dto.response.ProjectListResponse;
import org.devridge.api.domain.community.entity.Project;
import org.devridge.api.domain.community.exception.MyCommunityForbiddenException;
import org.devridge.api.domain.community.mapper.ProjectMapper;
import org.devridge.api.domain.community.repository.ProjectQuerydslRepository;
import org.devridge.api.domain.community.repository.ProjectRepository;
import org.devridge.api.domain.member.entity.Member;
import org.devridge.api.domain.member.repository.MemberRepository;
import org.devridge.api.domain.s3.service.S3Service;
import org.devridge.api.domain.skill.entity.ProjectSkill;
import org.devridge.api.domain.skill.entity.Skill;
import org.devridge.api.domain.skill.repository.ProjectSkillRepository;
import org.devridge.api.domain.skill.repository.SkillRepository;
import org.devridge.api.exception.common.DataNotFoundException;
import org.devridge.api.util.SecurityContextHolderUtil;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;
    private final MemberRepository memberRepository;
    private final S3Service s3Service;
    private final ProjectQuerydslRepository projectQuerydslRepository;
    private final ProjectSkillRepository projectSkillRepository;
    private final SkillRepository skillRepository;

    @Transactional
    public Long createProject(ProjectRequest request) {
        Long accessMemberId = SecurityContextHolderUtil.getMemberId();
        Member member = getMemberById(accessMemberId);

        Project project = projectMapper.toProject(request, member);
        Long projectId = projectRepository.save(project).getId();
        createProjectSkill(request.getSkillIds(), projectId);

        return projectId;
    }

    @Transactional
    public ProjectDetailResponse getProjectDetail(Long projectId) {
        Project project = getProjectById(projectId);

        List<ProjectSkill> projectSkills = project.getProjectSkills();
        List<String> skills = new ArrayList<>();
        for (ProjectSkill projectSkill : projectSkills) {
            skills.add(projectSkill.getSkill().getSkill());
        }

        projectRepository.updateView(projectId);
        return projectMapper.toProjectDetailResponse(project, skills);
    }

    public Slice<ProjectListResponse> getAllProject(Long lastId, Pageable pageable) {
        return projectQuerydslRepository.searchBySlice(lastId, pageable);
    }

    @Transactional
    public void updateProject(Long projectId, ProjectRequest request) {
        Long accessMemberId = SecurityContextHolderUtil.getMemberId();
        getMemberById(accessMemberId);
        Project project = getProjectById(projectId);

        if (!accessMemberId.equals(project.getMember().getId())) {
            throw new MyCommunityForbiddenException(403, "내가 작성하지 않은 글은 수정할 수 없습니다.");
        }

        projectSkillRepository.deleteByProjectId(projectId);


        if (request.getImages() != null && !request.getImages().isEmpty()) {
            String images = request.getImages().toString();

            project.updateProject(
                request.getTitle(),
                request.getContent(),
                request.getCategory(),
                images.substring(1, images.length() - 1),
                request.getMeeting(),
                request.getIsRecruiting()
            );

            createProjectSkill(request.getSkillIds(), projectId);
            return;
        }
        project.updateProject(
            request.getTitle(),
            request.getContent(),
            request.getCategory(),
            null,
            request.getMeeting(),
            request.getIsRecruiting()
        );

        createProjectSkill(request.getSkillIds(), projectId);

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

    @Transactional
    public void createProjectSkill(List<Long> skillIds, Long projectId) {
        List<Skill> skills = skillRepository.findSkillsByIds(skillIds);
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new DataNotFoundException());

        for (Skill skill : skills) {
            projectSkillRepository.save(
                ProjectSkill.builder()
                    .skill(skill)
                    .project(project)
                    .build()
            );
        }
    }

    private Project getProjectById(Long projectId) {
        return projectRepository.findById(projectId).orElseThrow(() -> new DataNotFoundException());
    }

    private Member getMemberById(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(() -> new DataNotFoundException());
    }
}
