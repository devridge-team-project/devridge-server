package org.devridge.api.domain.community.mapper;

import org.devridge.api.domain.community.dto.request.StudyRequest;
import org.devridge.api.domain.community.entity.Study;
import org.devridge.api.domain.member.entity.Member;
import org.springframework.stereotype.Component;

@Component
public class StudyMapper {

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
