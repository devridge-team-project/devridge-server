package org.devridge.api.application.community.study;

import lombok.RequiredArgsConstructor;
import org.devridge.api.domain.community.dto.request.StudyCommentRequest;
import org.devridge.api.domain.community.entity.Study;
import org.devridge.api.domain.community.entity.StudyComment;
import org.devridge.api.domain.member.entity.Member;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StudyCommentMapper {

    public StudyComment toStudyComment(Study study, Member member, StudyCommentRequest request) {
        return StudyComment.builder()
            .study(study)
            .content(request.getContent())
            .member(member)
            .build();
    }
}
