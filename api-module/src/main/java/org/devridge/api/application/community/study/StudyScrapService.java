package org.devridge.api.application.community.study;

import lombok.RequiredArgsConstructor;
import org.devridge.api.common.exception.common.DataNotFoundException;
import org.devridge.api.common.util.SecurityContextHolderUtil;
import org.devridge.api.domain.community.entity.Study;
import org.devridge.api.domain.community.entity.StudyScrap;
import org.devridge.api.domain.community.entity.id.StudyScrapId;
import org.devridge.api.domain.community.exception.MyCommunityForbiddenException;
import org.devridge.api.domain.member.entity.Member;
import org.devridge.api.infrastructure.community.study.StudyRepository;
import org.devridge.api.infrastructure.community.study.StudyScrapRepository;
import org.devridge.api.infrastructure.member.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class StudyScrapService {

    private final StudyScrapRepository studyScrapRepository;
    private final MemberRepository memberRepository;
    private final StudyRepository studyRepository;
    private final StudyScrapMapper studyScrapMapper;

    @Transactional
    public void createScrap(Long studyId) {
        Long accessMemberId = SecurityContextHolderUtil.getMemberId();
        Member member = getMemberById(accessMemberId);
        Study study = getStudyById(studyId);
        StudyScrapId studyScrapId = new StudyScrapId(accessMemberId, studyId);

        if (accessMemberId.equals(study.getMember().getId())) {
            throw new MyCommunityForbiddenException(403, "내가 작성한 글은 스크랩할 수 없습니다.");
        }

        studyScrapRepository.findById(studyScrapId).ifPresentOrElse(
            result -> {
                if (result.getIsDeleted()) {
                    studyScrapRepository.restoreById(studyScrapId);
                }

                if (!result.getIsDeleted()) {
                    studyScrapRepository.deleteById(studyScrapId);
                }
            },
            () -> {
                StudyScrap studyScrap = studyScrapMapper.toStudyScrap(study, member);
                studyScrapRepository.save(studyScrap);
            }
        );
    }

    private Member getMemberById(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(() -> new DataNotFoundException());
    }

    private Study getStudyById(Long studyId) {
        return studyRepository.findById(studyId).orElseThrow(() -> new DataNotFoundException());
    }
}
