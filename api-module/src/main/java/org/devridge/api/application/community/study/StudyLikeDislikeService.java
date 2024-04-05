package org.devridge.api.application.community.study;

import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.devridge.api.common.exception.common.DataNotFoundException;
import org.devridge.api.common.util.SecurityContextHolderUtil;
import org.devridge.api.domain.community.entity.LikeStatus;
import org.devridge.api.domain.community.entity.Study;
import org.devridge.api.domain.community.entity.StudyLikeDislike;
import org.devridge.api.domain.community.entity.id.StudyLikeDislikeId;
import org.devridge.api.domain.community.exception.MyCommunityForbiddenException;
import org.devridge.api.domain.member.entity.Member;
import org.devridge.api.infrastructure.community.study.StudyLikeDislikeRepository;
import org.devridge.api.infrastructure.community.study.StudyRepository;
import org.devridge.api.infrastructure.member.MemberRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudyLikeDislikeService {

    private final StudyLikeDislikeRepository studyLikeDislikeRepository;
    private final StudyRepository studyRepository;
    private final MemberRepository memberRepository;
    private final StudyLikeDislikeMapper studyLikeDislikeMapper;

    @Transactional
    public void createStudyLike(Long studyId) {
        Long accessMemberId = SecurityContextHolderUtil.getMemberId();
        Member member = getMemberById(accessMemberId);
        Study study = getStudyById(studyId);
        StudyLikeDislikeId studyLikeDislikeId = new StudyLikeDislikeId(member.getId(), study.getId());

        if (accessMemberId.equals(study.getMember().getId())) {
            throw new MyCommunityForbiddenException(403, "내가 작성한 글은 추천할 수 없습니다.");
        }

        studyLikeDislikeRepository.findById(studyLikeDislikeId).ifPresentOrElse(
            studyLikeDislike -> {
                LikeStatus status = studyLikeDislike.getStatus();

                if (status == LikeStatus.G) {
                    changeIsDeletedStatus(studyLikeDislike);
                }

                if (status == LikeStatus.B) {
                    if (studyLikeDislike.getIsDeleted()) {
                        studyLikeDislikeRepository.restoreById(studyLikeDislikeId);
                    }
                    studyLikeDislikeRepository.updateLikeDislike(studyLikeDislikeId, LikeStatus.G);
                }
            },
            () -> {
                StudyLikeDislike studyLikeDislike
                    = studyLikeDislikeMapper.toStudyLikeDislike(study, member, LikeStatus.G);
                studyLikeDislikeRepository.save(studyLikeDislike);
            }
        );
        updateLikeDislike(studyLikeDislikeId.getStudyId());
    }

    @Transactional
    public void createStudyDislike(Long studyId) {
        Long accessMemberId = SecurityContextHolderUtil.getMemberId();
        Member member = getMemberById(accessMemberId);
        Study study = getStudyById(studyId);
        StudyLikeDislikeId studyLikeDislikeId = new StudyLikeDislikeId(member.getId(), study.getId());

        if (accessMemberId.equals(study.getMember().getId())) {
            throw new MyCommunityForbiddenException(403, "내가 작성한 글은 비추천할 수 없습니다.");
        }

        studyLikeDislikeRepository.findById(studyLikeDislikeId).ifPresentOrElse(
            studyLikeDislike -> {
                LikeStatus status = studyLikeDislike.getStatus();

                if (status == LikeStatus.G) {
                    if (studyLikeDislike.getIsDeleted()) {
                        studyLikeDislikeRepository.restoreById(studyLikeDislikeId);
                    }
                    studyLikeDislikeRepository.updateLikeDislike(studyLikeDislikeId, LikeStatus.B);
                }

                if (status == LikeStatus.B) {
                    changeIsDeletedStatus(studyLikeDislike);
                }
            },
            () -> {
                StudyLikeDislike studyLikeDislike
                    = studyLikeDislikeMapper.toStudyLikeDislike(study, member, LikeStatus.B);
                studyLikeDislikeRepository.save(studyLikeDislike);
            }
        );
        updateLikeDislike(studyLikeDislikeId.getStudyId());
    }

    private void changeIsDeletedStatus(StudyLikeDislike studyLikeDislike) {

        if (studyLikeDislike.getIsDeleted()) {
            studyLikeDislikeRepository.restoreById(studyLikeDislike.getId());
        }
        if (!studyLikeDislike.getIsDeleted()) {
            studyLikeDislikeRepository.deleteById(studyLikeDislike.getId());
        }
    }

    private Member getMemberById(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(() -> new DataNotFoundException());
    }

    private Study getStudyById(Long studyId) {
        return studyRepository.findById(studyId).orElseThrow(() -> new DataNotFoundException());
    }

    private void updateLikeDislike(Long studyId) {
        Long likes = Long.valueOf(studyLikeDislikeRepository.countStudyLikeDislikeByStudyId(studyId, LikeStatus.G));
        Long disLikes = Long.valueOf(studyLikeDislikeRepository.countStudyLikeDislikeByStudyId(studyId, LikeStatus.B));
        studyRepository.updateLikeDislike(likes, disLikes, studyId);
    }
}
