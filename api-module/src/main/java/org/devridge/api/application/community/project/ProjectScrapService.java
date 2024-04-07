package org.devridge.api.application.community.project;

import lombok.RequiredArgsConstructor;
import org.devridge.api.common.exception.common.DataNotFoundException;
import org.devridge.api.common.util.SecurityContextHolderUtil;
import org.devridge.api.domain.community.entity.Project;
import org.devridge.api.domain.community.entity.ProjectScrap;
import org.devridge.api.domain.community.entity.id.ProjectScrapId;
import org.devridge.api.domain.community.exception.MyCommunityForbiddenException;
import org.devridge.api.domain.member.entity.Member;
import org.devridge.api.infrastructure.community.project.ProjectRepository;
import org.devridge.api.infrastructure.community.project.ProjectScrapRepository;
import org.devridge.api.infrastructure.member.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ProjectScrapService {

    private final ProjectScrapRepository projectScrapRepository;
    private final MemberRepository memberRepository;
    private final ProjectRepository projectRepository;
    private final ProjectScrapMapper projectScrapMapper;

    @Transactional
    public void createScrap(Long projectId) {
        Long accessMemberId = SecurityContextHolderUtil.getMemberId();
        Member member = getMemberById(accessMemberId);
        Project project = getProjectById(projectId);
        ProjectScrapId projectScrapId = new ProjectScrapId(accessMemberId, projectId);

        if (accessMemberId.equals(project.getMember().getId())) {
            throw new MyCommunityForbiddenException(403, "내가 작성한 글은 스크랩할 수 없습니다.");
        }

        projectScrapRepository.findById(projectScrapId).ifPresentOrElse(
            result -> {
                if (result.getIsDeleted()) {
                    projectScrapRepository.restoreById(projectScrapId);
                }

                if (!result.getIsDeleted()) {
                    projectScrapRepository.deleteById(projectScrapId);
                }
            },
            () -> {
                ProjectScrap projectScrap = projectScrapMapper.toProjectScrap(project, member);
                projectScrapRepository.save(projectScrap);
            }
        );
    }

    private Member getMemberById(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(() -> new DataNotFoundException());
    }

    private Project getProjectById(Long projectId) {
        return projectRepository.findById(projectId).orElseThrow(() -> new DataNotFoundException());
    }
}