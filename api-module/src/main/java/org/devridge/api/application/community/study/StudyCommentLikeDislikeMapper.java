package org.devridge.api.application.community.study;

import org.devridge.api.domain.community.entity.LikeStatus;
import org.devridge.api.domain.community.entity.StudyComment;
import org.devridge.api.domain.community.entity.StudyCommentLikeDislike;
import org.devridge.api.domain.member.entity.Member;
import org.springframework.stereotype.Component;

@Component
public class StudyCommentLikeDislikeMapper {

    public StudyCommentLikeDislike toStudyCommentLikeDislike(
        Member member,
        StudyComment comment,
        LikeStatus status
    ) {
        return StudyCommentLikeDislike.builder()
            .studyComment(comment)
            .member(member)
            .status(status)
            .build();
    }
}
