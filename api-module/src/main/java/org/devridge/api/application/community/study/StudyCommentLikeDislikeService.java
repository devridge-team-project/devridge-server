package org.devridge.api.application.community.study;

import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.devridge.api.common.exception.common.DataNotFoundException;
import org.devridge.api.common.util.SecurityContextHolderUtil;
import org.devridge.api.domain.community.entity.LikeStatus;
import org.devridge.api.domain.community.entity.Study;
import org.devridge.api.domain.community.entity.StudyComment;
import org.devridge.api.domain.community.entity.StudyCommentLikeDislike;
import org.devridge.api.domain.community.entity.id.StudyCommentLikeDislikeId;
import org.devridge.api.domain.community.exception.MyCommunityForbiddenException;
import org.devridge.api.domain.member.entity.Member;
import org.devridge.api.infrastructure.community.study.StudyCommentLikeDislikeRepository;
import org.devridge.api.infrastructure.community.study.StudyCommentRepository;
import org.devridge.api.infrastructure.community.study.StudyRepository;
import org.devridge.api.infrastructure.member.MemberRepository;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class StudyCommentLikeDislikeService {

    private final StudyCommentLikeDislikeRepository studyCommentLikeDislikeRepository;
    private final StudyCommentRepository studyCommentRepository;
    private final MemberRepository memberRepository;
    private final StudyCommentLikeDislikeMapper studyCommentLikeDislikeMapper;
    private final StudyRepository studyRepository;

    @Transactional
    public void createStudyCommentLike(Long studyId, Long commentId) {
        Long accessMemberId = SecurityContextHolderUtil.getMemberId();
        Member member = getMemberById(accessMemberId);
        Study study = getStudyById(studyId);
        StudyComment comment = getCommentById(commentId);
        StudyCommentLikeDislikeId studyCommentLikeDislikeId =
            new StudyCommentLikeDislikeId(member.getId(), comment.getId());

        if (accessMemberId.equals(comment.getMember().getId())) {
            throw new MyCommunityForbiddenException(403, "내가 작성한 글은 추천할 수 없습니다.");
        }

        studyCommentLikeDislikeRepository.findById(studyCommentLikeDislikeId).ifPresentOrElse(
            StudyCommentLikeDislike -> {
                LikeStatus status = StudyCommentLikeDislike.getStatus();

                if (status == LikeStatus.G) {
                    changeIsDeletedStatus(StudyCommentLikeDislike);
                }

                if (status == LikeStatus.B) {
                    if (StudyCommentLikeDislike.getIsDeleted()) {
                        studyCommentLikeDislikeRepository.restoreById(studyCommentLikeDislikeId);
                    }
                    studyCommentLikeDislikeRepository.updateLikeDislike(studyCommentLikeDislikeId,
                        LikeStatus.G);
                }
            },
            () -> {
                StudyCommentLikeDislike commentLikeDislike =
                    studyCommentLikeDislikeMapper.toStudyCommentLikeDislike(member, comment, LikeStatus.G);
                studyCommentLikeDislikeRepository.save(commentLikeDislike);
            }
        );
        updateLikeDislike(studyCommentLikeDislikeId.getCommentId());
    }

    @Transactional
    public void createStudyCommentDislike(Long studyId, Long commentId) {
        Long accessMemberId = SecurityContextHolderUtil.getMemberId();
        Member member = getMemberById(accessMemberId);
        Study study = getStudyById(studyId);
        StudyComment comment = getCommentById(commentId);
        StudyCommentLikeDislikeId studyCommentLikeDislikeId =
            new StudyCommentLikeDislikeId(member.getId(), comment.getId());

        if (accessMemberId.equals(comment.getMember().getId())) {
            throw new MyCommunityForbiddenException(403, "내가 작성한 글은 비추천할 수 없습니다.");
        }

        studyCommentLikeDislikeRepository.findById(studyCommentLikeDislikeId).ifPresentOrElse(
            StudyCommentLikeDislike -> {
                LikeStatus status = StudyCommentLikeDislike.getStatus();

                if (status == LikeStatus.B) {
                    changeIsDeletedStatus(StudyCommentLikeDislike);
                }

                if (status == LikeStatus.G) {
                    if (StudyCommentLikeDislike.getIsDeleted()) {
                        studyCommentLikeDislikeRepository.restoreById(studyCommentLikeDislikeId);
                    }
                    studyCommentLikeDislikeRepository.updateLikeDislike(studyCommentLikeDislikeId, LikeStatus.B);
                }
            },
            () -> {
                StudyCommentLikeDislike commentLikeDislike =
                    studyCommentLikeDislikeMapper.toStudyCommentLikeDislike(member, comment, LikeStatus.B);
                studyCommentLikeDislikeRepository.save(commentLikeDislike);
            }
        );
        updateLikeDislike(studyCommentLikeDislikeId.getCommentId());
    }

    private void updateLikeDislike(Long studyId) {
        Long likes = Long.valueOf(studyCommentLikeDislikeRepository.countStudyLikeDislikeById(studyId, LikeStatus.G));
        Long disLikes = Long.valueOf(studyCommentLikeDislikeRepository.countStudyLikeDislikeById(studyId, LikeStatus.B));
        studyCommentRepository.updateLikeDislike(likes, disLikes, studyId);
    }

    private void changeIsDeletedStatus(StudyCommentLikeDislike studyCommentLikeDislike) {
        if (studyCommentLikeDislike.getIsDeleted()) {
            studyCommentLikeDislikeRepository.restoreById(studyCommentLikeDislike.getId());
        }
        if (!studyCommentLikeDislike.getIsDeleted()) {
            studyCommentLikeDislikeRepository.deleteById(studyCommentLikeDislike.getId());
        }
    }

    private Member getMemberById(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(() -> new DataNotFoundException());
    }

    private StudyComment getCommentById(Long commentId) {
        return studyCommentRepository.findById(commentId).orElseThrow(() -> new DataNotFoundException());
    }

    private Study getStudyById(Long studyId) {
        return studyRepository.findById(studyId).orElseThrow(() -> new DataNotFoundException());
    }
}
