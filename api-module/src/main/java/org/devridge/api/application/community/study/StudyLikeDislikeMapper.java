package org.devridge.api.application.community.study;

import org.devridge.api.domain.community.entity.LikeStatus;
import org.devridge.api.domain.community.entity.Study;
import org.devridge.api.domain.community.entity.StudyLikeDislike;
import org.devridge.api.domain.member.entity.Member;
import org.springframework.stereotype.Component;

@Component
public class StudyLikeDislikeMapper {

    public StudyLikeDislike toStudyLikeDislike(Study study, Member member, LikeStatus status) {
        return StudyLikeDislike.builder()
            .study(study)
            .member(member)
            .status(status)
            .build();
    }
}
