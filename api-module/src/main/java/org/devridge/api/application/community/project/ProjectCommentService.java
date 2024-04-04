package org.devridge.api.application.community.project;

import lombok.RequiredArgsConstructor;
import org.devridge.api.common.exception.common.DataNotFoundException;
import org.devridge.api.common.util.SecurityContextHolderUtil;
import org.devridge.api.domain.community.dto.request.ProjectCommentRequest;
import org.devridge.api.domain.community.entity.Project;
import org.devridge.api.domain.community.entity.ProjectComment;
import org.devridge.api.domain.member.entity.Member;
import org.devridge.api.infrastructure.community.project.ProjectCommentRepository;
import org.devridge.api.infrastructure.community.project.ProjectRepository;
import org.devridge.api.infrastructure.member.MemberRepository;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProjectCommentService {

    private final ProjectCommentRepository projectCommentRepository;
    private final ProjectCommentMapper projectCommentMapper;
    private final ProjectRepository projectRepository;
    private final MemberRepository memberRepository;

    public Long createProjectComment(Long projectId, ProjectCommentRequest commentRequest) {
        Project project = getProjectById(projectId);
        Long accessMemberId = SecurityContextHolderUtil.getMemberId();
        Member member = getMemberById(accessMemberId);

        ProjectComment projectComment =
                projectCommentMapper.toProjectComment(project, member, commentRequest);
        return projectCommentRepository.save(projectComment).getId();
    }

    private Project getProjectById(Long projectId) {
        return projectRepository.findById(projectId).orElseThrow(() -> new DataNotFoundException());
    }

    private Member getMemberById(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(() -> new DataNotFoundException());
    }

}

