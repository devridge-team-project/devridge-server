package org.devridge.api.application.community.project;

import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.devridge.api.common.exception.common.DataNotFoundException;
import org.devridge.api.common.util.SecurityContextHolderUtil;
import org.devridge.api.domain.community.entity.LikeStatus;
import org.devridge.api.domain.community.entity.Project;
import org.devridge.api.domain.community.entity.ProjectComment;
import org.devridge.api.domain.community.entity.ProjectCommentLikeDislike;
import org.devridge.api.domain.community.entity.id.ProjectCommentLikeDislikeId;
import org.devridge.api.domain.community.exception.MyCommunityForbiddenException;
import org.devridge.api.domain.member.entity.Member;
import org.devridge.api.infrastructure.community.project.ProjectCommentLikeDislikeRepository;
import org.devridge.api.infrastructure.community.project.ProjectCommentRepository;
import org.devridge.api.infrastructure.community.project.ProjectRepository;
import org.devridge.api.infrastructure.member.MemberRepository;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProjectCommentLikeDislikeService {

    private final ProjectCommentLikeDislikeRepository projectCommentLikeDislikeRepository;
    private final ProjectCommentRepository projectCommentRepository;
    private final MemberRepository memberRepository;
    private final ProjectCommentLikeDislikeMapper projectCommentLikeDislikeMapper;
    private final ProjectRepository projectRepository;

    @Transactional
    public void createProjectCommentLike(Long projectId, Long commentId) {
        Long accessMemberId = SecurityContextHolderUtil.getMemberId();
        Member member = getMemberById(accessMemberId);
        Project project = getProjectById(projectId);
        ProjectComment comment = getCommentById(commentId);
        ProjectCommentLikeDislikeId projectCommentLikeDislikeId =
                new ProjectCommentLikeDislikeId(member.getId(), comment.getId());

        if (accessMemberId.equals(comment.getMember().getId())) {
            throw new MyCommunityForbiddenException(403, "내가 작성한 글은 추천할 수 없습니다.");
        }

        projectCommentLikeDislikeRepository.findById(projectCommentLikeDislikeId).ifPresentOrElse(
            ProjectCommentLikeDislike -> {
                LikeStatus status = ProjectCommentLikeDislike.getStatus();

                if (status == LikeStatus.G) {
                    changeIsDeletedStatus(ProjectCommentLikeDislike);
                }

                if (status == LikeStatus.B) {
                    if (ProjectCommentLikeDislike.getIsDeleted()) {
                        projectCommentLikeDislikeRepository.restoreById(projectCommentLikeDislikeId);
                    }
                    projectCommentLikeDislikeRepository.updateLikeDislike(projectCommentLikeDislikeId,
                        LikeStatus.G);
                }
            },
            () -> {
                ProjectCommentLikeDislike commentLikeDislike =
                    projectCommentLikeDislikeMapper.toProjectCommentLikeDislike(member, comment, LikeStatus.G);
                projectCommentLikeDislikeRepository.save(commentLikeDislike);
            }
        );
        updateLikeDislike(projectCommentLikeDislikeId.getCommentId());
    }

    @Transactional
    public void createProjectCommentDislike(Long projectId, Long commentId) {
        Long accessMemberId = SecurityContextHolderUtil.getMemberId();
        Member member = getMemberById(accessMemberId);
        Project project = getProjectById(projectId);
        ProjectComment comment = getCommentById(commentId);
        ProjectCommentLikeDislikeId projectCommentLikeDislikeId =
                new ProjectCommentLikeDislikeId(member.getId(), comment.getId());

        if (accessMemberId.equals(comment.getMember().getId())) {
            throw new MyCommunityForbiddenException(403, "내가 작성한 글은 비추천할 수 없습니다.");
        }

        projectCommentLikeDislikeRepository.findById(projectCommentLikeDislikeId).ifPresentOrElse(
            ProjectCommentLikeDislike -> {
                LikeStatus status = ProjectCommentLikeDislike.getStatus();

                if (status == LikeStatus.B) {
                    changeIsDeletedStatus(ProjectCommentLikeDislike);
                }

                if (status == LikeStatus.G) {
                    if (ProjectCommentLikeDislike.getIsDeleted()) {
                        projectCommentLikeDislikeRepository.restoreById(projectCommentLikeDislikeId);
                    }
                    projectCommentLikeDislikeRepository.updateLikeDislike(projectCommentLikeDislikeId, LikeStatus.B);
                }
            },
            () -> {
                ProjectCommentLikeDislike commentLikeDislike =
                    projectCommentLikeDislikeMapper.toProjectCommentLikeDislike(member, comment, LikeStatus.B);
                projectCommentLikeDislikeRepository.save(commentLikeDislike);
            }
        );
        updateLikeDislike(projectCommentLikeDislikeId.getCommentId());
    }

    private void updateLikeDislike(Long projectId) {
        Long likes = Long.valueOf(projectCommentLikeDislikeRepository.countProjectLikeDislikeById(projectId, LikeStatus.G));
        Long disLikes = Long.valueOf(projectCommentLikeDislikeRepository.countProjectLikeDislikeById(projectId, LikeStatus.B));
        projectCommentRepository.updateLikeDislike(likes, disLikes, projectId);
    }

    private void changeIsDeletedStatus(ProjectCommentLikeDislike projectCommentLikeDislike) {
        if (projectCommentLikeDislike.getIsDeleted()) {
            projectCommentLikeDislikeRepository.restoreById(projectCommentLikeDislike.getId());
        }
        if (!projectCommentLikeDislike.getIsDeleted()) {
            projectCommentLikeDislikeRepository.deleteById(projectCommentLikeDislike.getId());
        }
    }

    private Member getMemberById(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(() -> new DataNotFoundException());
    }

    private ProjectComment getCommentById(Long commentId) {
        return projectCommentRepository.findById(commentId).orElseThrow(() -> new DataNotFoundException());
    }

    private Project getProjectById(Long projectId) {
        return projectRepository.findById(projectId).orElseThrow(() -> new DataNotFoundException());
    }
}
