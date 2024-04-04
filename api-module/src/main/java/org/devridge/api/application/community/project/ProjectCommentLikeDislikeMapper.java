package org.devridge.api.application.community.project;

import org.devridge.api.domain.community.entity.LikeStatus;
import org.devridge.api.domain.community.entity.ProjectComment;
import org.devridge.api.domain.community.entity.ProjectCommentLikeDislike;
import org.devridge.api.domain.member.entity.Member;
import org.springframework.stereotype.Component;

@Component
public class ProjectCommentLikeDislikeMapper {

    public ProjectCommentLikeDislike toProjectCommentLikeDislike(
        Member member,
        ProjectComment comment,
        LikeStatus status
    ) {
        return ProjectCommentLikeDislike.builder()
            .projectComment(comment)
            .member(member)
            .status(status)
            .build();
    }
}