package org.devridge.api.application.community.study;

import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.devridge.api.application.s3.S3Service;
import org.devridge.api.common.exception.common.DataNotFoundException;
import org.devridge.api.common.util.SecurityContextHolderUtil;
import org.devridge.api.domain.community.dto.request.StudyRequest;
import org.devridge.api.domain.community.dto.response.StudyDetailResponse;
import org.devridge.api.domain.community.dto.response.StudyListResponse;
import org.devridge.api.domain.community.entity.Study;
import org.devridge.api.domain.community.exception.MyCommunityForbiddenException;
import org.devridge.api.domain.member.entity.Member;
import org.devridge.api.infrastructure.community.study.StudyQuerydslRepository;
import org.devridge.api.infrastructure.community.study.StudyRepository;
import org.devridge.api.infrastructure.member.MemberRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StudyService {

    private final StudyRepository studyRepository;
    private final MemberRepository memberRepository;
    private final StudyMapper studyMapper;
    private final S3Service s3Service;
    private final StudyQuerydslRepository studyQuerydslRepository;

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

    public List<StudyListResponse> getAllStudy(Long lastId, Pageable pageable) {
        List<Study> studies = studyQuerydslRepository.searchByStudy(lastId, pageable);
        return studyMapper.toStudyListResponses(studies);

    }

    @Transactional
    public void updateStudy(Long studyId, StudyRequest request) {
        Long accessMemberId = SecurityContextHolderUtil.getMemberId();
        getMemberById(accessMemberId);
        Study study = getStudyById(studyId);

        if (!accessMemberId.equals(study.getMember().getId())) {
            throw new MyCommunityForbiddenException(403, "내가 작성하지 않은 글은 수정할 수 없습니다.");
        }

        if (request.getImages() != null && !request.getImages().isEmpty()) {
            String images = request.getImages().toString();

            study.updateStudy(
                request.getTitle(),
                request.getContent(),
                request.getCategory().getValue(),
                images.substring(1, images.length() - 1),
                request.getLocation()
            );
            return;
        }

        study.updateStudy(
            request.getTitle(),
            request.getContent(),
            request.getCategory().getValue(),
            null,
            request.getLocation()
        );
    }

    @Transactional
    public void deleteStudy(Long studyId) {
        Long accessMemberId = SecurityContextHolderUtil.getMemberId();
        Study study = getStudyById(studyId);

        if (!accessMemberId.equals(study.getMember().getId())) {
            throw new MyCommunityForbiddenException(403, "내가 작성하지 않은 글은 삭제할 수 없습니다.");
        }

        studyRepository.delete(study);

        if (study.getImages() != null) {
            List<String> images = Arrays.asList(study.getImages().split(", "));
            s3Service.deleteAllImage(images);
        }
    }

    private Member getMemberById(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(() -> new DataNotFoundException());
    }

    private Study getStudyById(Long studyId) {
        return studyRepository.findById(studyId).orElseThrow(() -> new DataNotFoundException());
    }
}
