package org.devridge.api.application.community.project;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.devridge.api.common.exception.common.DataNotFoundException;
import org.devridge.api.common.util.SecurityContextHolderUtil;
import org.devridge.api.domain.community.dto.request.ProjectCommentRequest;
import org.devridge.api.domain.community.dto.response.ProjectCommentResponse;
import org.devridge.api.domain.community.entity.Project;
import org.devridge.api.domain.community.entity.ProjectComment;
import org.devridge.api.domain.community.exception.MyCommunityForbiddenException;
import org.devridge.api.domain.member.entity.Member;
import org.devridge.api.infrastructure.community.project.ProjectCommentQuerydslRepository;
import org.devridge.api.infrastructure.community.project.ProjectCommentRepository;
import org.devridge.api.infrastructure.community.project.ProjectRepository;
import org.devridge.api.infrastructure.member.MemberRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProjectCommentService {

    private final ProjectCommentRepository projectCommentRepository;
    private final ProjectCommentMapper projectCommentMapper;
    private final ProjectRepository projectRepository;
    private final MemberRepository memberRepository;
    private final ProjectCommentQuerydslRepository projectCommentQuerydslRepository;

    public Long createProjectComment(Long projectId, ProjectCommentRequest commentRequest) {
        Project project = getProjectById(projectId);
        Long accessMemberId = SecurityContextHolderUtil.getMemberId();
        Member member = getMemberById(accessMemberId);

        ProjectComment projectComment =
                projectCommentMapper.toProjectComment(project, member, commentRequest);
        return projectCommentRepository.save(projectComment).getId();
    }

    public List<ProjectCommentResponse> getAllProjectComment(Long projectId, Long lastId, Pageable pageable) {
        return projectCommentQuerydslRepository.searchBySlice(projectId, lastId, pageable);
    }

    public void updateComment(Long projectId, Long commentId, ProjectCommentRequest commentRequest) {
        getProjectById(projectId);
        Long accessMemberId = SecurityContextHolderUtil.getMemberId();
        getMemberById(accessMemberId);
        ProjectComment comment = getProjectComment(commentId);

        if (!comment.getMember().getId().equals(accessMemberId)) {
            throw new MyCommunityForbiddenException(403, "내가 작성하지 않은 글은 수정할 수 없습니다.");
        }

        comment.updateComment(commentRequest.getContent());
        projectCommentRepository.save(comment);
    }

    public void deleteComment(Long projectId, Long commentId) {
        getProjectById(projectId);
        Long accessMemberId = SecurityContextHolderUtil.getMemberId();
        getMemberById(accessMemberId);
        ProjectComment comment = getProjectComment(commentId);

        if (!comment.getMember().getId().equals(accessMemberId)) {
            throw new MyCommunityForbiddenException(403, "내가 작성하지 않은 글은 삭제할 수 없습니다.");
        }

        projectCommentRepository.deleteById(commentId);
    }

    private ProjectComment getProjectComment(Long commentId) {
        return projectCommentRepository.findById(commentId).orElseThrow(() -> new DataNotFoundException());
    }

    private Project getProjectById(Long projectId) {
        return projectRepository.findById(projectId).orElseThrow(() -> new DataNotFoundException());
    }

    private Member getMemberById(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(() -> new DataNotFoundException());
    }

}

