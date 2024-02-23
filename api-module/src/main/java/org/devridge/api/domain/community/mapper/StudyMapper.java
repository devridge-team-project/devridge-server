package org.devridge.api.domain.community.mapper;

import lombok.RequiredArgsConstructor;
import org.devridge.api.domain.community.dto.request.StudyRequest;
import org.devridge.api.domain.community.dto.response.MemberInfoResponse;
import org.devridge.api.domain.community.dto.response.StudyDetailResponse;
import org.devridge.api.domain.community.entity.Study;
import org.devridge.api.domain.member.entity.Member;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StudyMapper {

    private final MemberInfoMapper memberInfoMapper;

    public StudyDetailResponse toStudyDetailResponse(Study study) {
        Member member = study.getMember();
        MemberInfoResponse memberInfoResponse = memberInfoMapper.toMemberInfoResponse(member);
        return StudyDetailResponse.builder()
            .studyId(study.getId())
            .memberInfoResponse(memberInfoResponse)
            .title(study.getTitle())
            .content(study.getContent())
            .likes(study.getLikes())
            .dislikes(study.getDislikes())
            .views(study.getViews() + 1)
            .createdAt(study.getCreatedAt())
            .updatedAt(study.getUpdatedAt())
            .build();
    }

    public Study toStudy(Member member, StudyRequest studyRequest) {
        if (studyRequest.getImages() != null && !studyRequest.getImages().isEmpty()) {
            String images = studyRequest.getImages().toString();
            return Study.builder()
                    .member(member)
                    .title(studyRequest.getTitle())
                    .content(studyRequest.getContent())
                    .location(studyRequest.getLocation())
                    .category(studyRequest.getCategory())
                    .images(images.substring(1, images.length() - 1))
                    .totalPeople(studyRequest.getTotalPeople())
                    .currentPeople(studyRequest.getCurrentPeople())
                    .build();
        }
        return Study.builder()
                .title(studyRequest.getTitle())
                .content(studyRequest.getContent())
                .location(studyRequest.getLocation())
                .category(studyRequest.getCategory())
                .totalPeople(studyRequest.getTotalPeople())
                .currentPeople(studyRequest.getCurrentPeople())
                .build();
    }
}
