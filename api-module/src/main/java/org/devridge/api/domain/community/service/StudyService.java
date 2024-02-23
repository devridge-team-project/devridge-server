package org.devridge.api.domain.community.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.devridge.api.domain.community.dto.request.StudyRequest;
import org.devridge.api.domain.community.dto.response.StudyDetailResponse;
import org.devridge.api.domain.community.dto.response.StudyListResponse;
import org.devridge.api.domain.community.entity.Study;
import org.devridge.api.domain.community.mapper.StudyMapper;
import org.devridge.api.domain.community.repository.StudyRepository;
import org.devridge.api.domain.member.entity.Member;
import org.devridge.api.domain.member.repository.MemberRepository;
import org.devridge.api.exception.common.DataNotFoundException;
import org.devridge.api.util.SecurityContextHolderUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StudyService {

    private final StudyRepository studyRepository;
    private final MemberRepository memberRepository;
    private final StudyMapper studyMapper;

    public Long createStudy(StudyRequest studyRequest) {
        Long accessMemberId = SecurityContextHolderUtil.getMemberId();
        Member member = getMemberById(accessMemberId);
        Study study = studyMapper.toStudy(member, studyRequest);
        return studyRepository.save(study).getId();

    }

    @Transactional
    public StudyDetailResponse getStudyDetail(Long studyId) {
        Study study = getStudyById(studyId);
        studyRepository.updateView(studyId);
        return studyMapper.toStudyDetailResponse(study);
    }

    public List<StudyListResponse> getAllStudy() {
        List<Study> studies = studyRepository.findAll();
        return studyMapper.toStudyListResponses(studies);
    }

    private Member getMemberById(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(() -> new DataNotFoundException());
    }

    private Study getStudyById(Long studyId) {
        return studyRepository.findById(studyId).orElseThrow(() -> new DataNotFoundException());
    }
}
