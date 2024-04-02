package org.devridge.api.application.community.project;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.devridge.api.application.s3.S3Service;
import org.devridge.api.common.exception.common.DataNotFoundException;
import org.devridge.api.common.util.SecurityContextHolderUtil;
import org.devridge.api.domain.community.dto.request.ProjectRequest;
import org.devridge.api.domain.community.dto.response.ProjectDetailResponse;
import org.devridge.api.domain.community.dto.response.ProjectListResponse;
import org.devridge.api.domain.community.dto.response.SkillResponse;
import org.devridge.api.domain.community.entity.Project;
import org.devridge.api.domain.community.exception.MyCommunityForbiddenException;
import org.devridge.api.domain.member.entity.Member;
import org.devridge.api.domain.skill.entity.ProjectSkill;
import org.devridge.api.domain.skill.entity.Skill;
import org.devridge.api.infrastructure.community.project.ProjectBulkRepository;
import org.devridge.api.infrastructure.community.project.ProjectQuerydslRepository;
import org.devridge.api.infrastructure.community.project.ProjectRepository;
import org.devridge.api.infrastructure.member.MemberRepository;
import org.devridge.api.infrastructure.skill.ProjectSkillRepository;
import org.devridge.api.infrastructure.skill.SkillRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
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
    private final ProjectBulkRepository projectBulkRepository;

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
        List<ProjectListResponse> projectListResponses = projectQuerydslRepository.searchByProject(lastId, pageable);
        List<Long> projectIds = toProjectIds(projectListResponses);
        List<ProjectSkill> projectSkills = projectQuerydslRepository.findProjectSkillsInProjectIds(projectIds);
        List<ProjectListResponse> groupedByProjectListResponses = groupByProjectId(projectSkills, projectListResponses);
        return checkLastPage(pageable, groupedByProjectListResponses);
    }

    private List<ProjectListResponse> groupByProjectId(List<ProjectSkill> projectSkills, List<ProjectListResponse> projectListResponses) {
        Map<Long, List<SkillResponse>> groupedByProjectId = new HashMap<>();

        for (ProjectSkill projectSkill : projectSkills) {
            Long projectId = projectSkill.getId().getProjectId();
            SkillResponse skillResponse = SkillResponse.builder()
                .id(projectSkill.getSkill().getId())
                .skill(projectSkill.getSkill().getSkill())
                .build();

            if (groupedByProjectId.containsKey(projectId)) {
                groupedByProjectId.get(projectId).add(skillResponse);
            } else {
                List<SkillResponse> projectIdBySkill = new ArrayList<>();
                projectIdBySkill.add(skillResponse);
                groupedByProjectId.put(projectId, projectIdBySkill);
            }
        }

        for (ProjectListResponse projectListResponse : projectListResponses) {
            groupedByProjectId.forEach((projectId, skillResponse) -> {
                if (Objects.equals(projectId, projectListResponse.getId())) {
                    projectListResponse.setSkills(skillResponse);
                }
            });
        }

        return projectListResponses;
    }
    private Slice<ProjectListResponse> checkLastPage(Pageable pageable, List<ProjectListResponse> results) {

        boolean hasNext = false;

        // 조회한 결과 개수가 요청한 페이지 사이즈보다 크면 뒤에 더 있음, next = true
        if (results.size() > pageable.getPageSize()) {
            hasNext = true;
            results.remove(pageable.getPageSize());
        }

        return new SliceImpl<>(results, pageable, hasNext);
    }

    private List<Long> toProjectIds(List<ProjectListResponse> projectListResponses) {
        return projectListResponses.stream()
            .map(projectId -> projectId.getId())
            .collect(Collectors.toList());
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

        String roles = null;
        String images = null;

        if (request.getRoles() != null && !request.getRoles().isEmpty()) {
            roles = request.getRoles().toString();
            roles = roles.substring(1, roles.length() - 1);
        }

        if (request.getImages() != null && !request.getImages().isEmpty()) {
            images = request.getImages().toString();
            images = images.substring(1, images.length() - 1);
        }

        project.updateProject(
            request.getTitle(),
            request.getContent(),
            roles,
            images,
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

        projectBulkRepository.saveAll(skills, project);
    }

    private Project getProjectById(Long projectId) {
        return projectRepository.findById(projectId).orElseThrow(() -> new DataNotFoundException());
    }

    private Member getMemberById(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(() -> new DataNotFoundException());
    }
}
