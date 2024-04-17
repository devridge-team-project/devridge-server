package org.devridge.api.application.community.study;

import static org.devridge.api.common.util.MemberUtil.toMember;

import java.util.ArrayList;
import java.util.List;

import org.devridge.api.common.dto.UserInformation;
import org.devridge.api.domain.community.dto.request.StudyRequest;
import org.devridge.api.domain.community.dto.response.StudyDetailResponse;
import org.devridge.api.domain.community.dto.response.StudyListResponse;
import org.devridge.api.domain.community.entity.Study;
import org.devridge.api.domain.member.entity.Member;

import org.springframework.stereotype.Component;

@Component
public class StudyMapper {

    public StudyDetailResponse toStudyDetailResponse(Study study) {
        Member member = study.getMember();
        UserInformation userInformation = toMember(member);

        return StudyDetailResponse.builder()
            .studyId(study.getId())
            .userInformation(userInformation)
            .title(study.getTitle())
            .content(study.getContent())
            .likes(study.getLikes())
            .dislikes(study.getDislikes())
            .views(study.getViews() + 1)
            .createdAt(study.getCreatedAt())
            .updatedAt(study.getUpdatedAt())
            .location(study.getLocation())
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
                    .category(studyRequest.getCategory().getValue())
                    .images(images.substring(1, images.length() - 1))
                    .build();
        }

        return Study.builder()
                .member(member)
                .title(studyRequest.getTitle())
                .content(studyRequest.getContent())
                .location(studyRequest.getLocation())
                .category(studyRequest.getCategory().getValue())
                .build();
    }

    public StudyListResponse toStudyListResponse(Study study) {
        return StudyListResponse.builder()
            .id(study.getId())
            .likes(study.getLikes())
            .category(study.getCategory())
            .title(study.getTitle())
            .content(study.getContent())
            .views(study.getViews())
            .dislikes(study.getDislikes())
            .location(study.getLocation())
            .images(study.getImages())
            .build();
    }

    public List<StudyListResponse> toStudyListResponses(List<Study> studies) {
        List<StudyListResponse> studyListResponses = new ArrayList<>();

        for (Study study : studies) {
            studyListResponses.add(
                toStudyListResponse(study)
            );
        }

        return studyListResponses;
    }
}
