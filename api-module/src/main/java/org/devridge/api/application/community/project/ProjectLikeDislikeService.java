package org.devridge.api.application.community.project;

import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.devridge.api.common.exception.common.DataNotFoundException;
import org.devridge.api.common.util.SecurityContextHolderUtil;
import org.devridge.api.domain.community.entity.LikeStatus;
import org.devridge.api.domain.community.entity.Project;
import org.devridge.api.domain.community.entity.ProjectLikeDislike;
import org.devridge.api.domain.community.entity.id.ProjectLikeDislikeId;
import org.devridge.api.domain.community.exception.MyCommunityForbiddenException;
import org.devridge.api.domain.member.entity.Member;
import org.devridge.api.infrastructure.community.project.ProjectLikeDislikeRepository;
import org.devridge.api.infrastructure.community.project.ProjectRepository;
import org.devridge.api.infrastructure.member.MemberRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProjectLikeDislikeService {

    private final ProjectLikeDislikeRepository projectLikeDislikeRepository;
    private final ProjectRepository projectRepository;
    private final MemberRepository memberRepository;
    private final ProjectLikeDislikeMapper projectLikeDislikeMapper;

    @Transactional
    public void createProjectLike(Long projectId) {
        Long accessMemberId = SecurityContextHolderUtil.getMemberId();
        Member member = getMemberById(accessMemberId);
        Project project = getProjectById(projectId);
        ProjectLikeDislikeId projectLikeDislikeId = new ProjectLikeDislikeId(member.getId(), project.getId());

        if (accessMemberId.equals(project.getMember().getId())) {
            throw new MyCommunityForbiddenException(403, "내가 작성한 글은 추천할 수 없습니다.");
        }

        projectLikeDislikeRepository.findById(projectLikeDislikeId).ifPresentOrElse(
            projectLikeDislike -> {
                LikeStatus status = projectLikeDislike.getStatus();

                if (status == LikeStatus.G) {
                    changeIsDeletedStatus(projectLikeDislike);
                }

                if (status == LikeStatus.B) {
                    if (projectLikeDislike.getIsDeleted()) {
                        projectLikeDislikeRepository.restoreById(projectLikeDislikeId);
                    }
                    projectLikeDislikeRepository.updateLikeDislike(projectLikeDislikeId, LikeStatus.G);
                }
            },
            () -> {
                ProjectLikeDislike projectLikeDislike
                    = projectLikeDislikeMapper.toProjectLikeDislike(project, member, LikeStatus.G);
                projectLikeDislikeRepository.save(projectLikeDislike);
            }
        );
        updateLikeDislike(projectLikeDislikeId.getProjectId());
    }

    @Transactional
    public void createProjectDislike(Long projectId) {
        Long accessMemberId = SecurityContextHolderUtil.getMemberId();
        Member member = getMemberById(accessMemberId);
        Project project = getProjectById(projectId);
        ProjectLikeDislikeId projectLikeDislikeId = new ProjectLikeDislikeId(member.getId(), project.getId());

        if (accessMemberId.equals(project.getMember().getId())) {
            throw new MyCommunityForbiddenException(403, "내가 작성한 글은 비추천할 수 없습니다.");
        }

        projectLikeDislikeRepository.findById(projectLikeDislikeId).ifPresentOrElse(
            projectLikeDislike -> {
                LikeStatus status = projectLikeDislike.getStatus();

                if (status == LikeStatus.G) {
                    if (projectLikeDislike.getIsDeleted()) {
                        projectLikeDislikeRepository.restoreById(projectLikeDislikeId);
                    }
                    projectLikeDislikeRepository.updateLikeDislike(projectLikeDislikeId, LikeStatus.B);
                }

                if (status == LikeStatus.B) {
                    changeIsDeletedStatus(projectLikeDislike);
                }
            },
            () -> {
                ProjectLikeDislike projectLikeDislike
                    = projectLikeDislikeMapper.toProjectLikeDislike(project, member, LikeStatus.B);
                projectLikeDislikeRepository.save(projectLikeDislike);
            }
        );
        updateLikeDislike(projectLikeDislikeId.getProjectId());
    }

    private void changeIsDeletedStatus(ProjectLikeDislike projectLikeDislike) {

        if (projectLikeDislike.getIsDeleted()) {
            projectLikeDislikeRepository.restoreById(projectLikeDislike.getId());
        }
        if (!projectLikeDislike.getIsDeleted()) {
            projectLikeDislikeRepository.deleteById(projectLikeDislike.getId());
        }
    }

    private Member getMemberById(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(() -> new DataNotFoundException());
    }

    private Project getProjectById(Long projectId) {
        return projectRepository.findById(projectId).orElseThrow(() -> new DataNotFoundException());
    }

    private void updateLikeDislike(Long projectId) {
        Long likes = Long.valueOf(projectLikeDislikeRepository.countProjectLikeDislikeByProjectId(projectId, LikeStatus.G));
        Long disLikes = Long.valueOf(projectLikeDislikeRepository.countProjectLikeDislikeByProjectId(projectId, LikeStatus.B));
        projectRepository.updateLikeDislike(likes, disLikes, projectId);
    }
}
