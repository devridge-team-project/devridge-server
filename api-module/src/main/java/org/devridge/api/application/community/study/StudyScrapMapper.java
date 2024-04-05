package org.devridge.api.application.community.study;

import org.devridge.api.domain.community.entity.Study;
import org.devridge.api.domain.community.entity.StudyScrap;
import org.devridge.api.domain.member.entity.Member;
import org.springframework.stereotype.Component;

@Component
public class StudyScrapMapper {

    public StudyScrap toStudyScrap(Study study, Member member) {
        return StudyScrap.builder()
            .study(study)
            .member(member)
            .build();
    }
}