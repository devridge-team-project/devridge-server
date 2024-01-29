package org.devridge.api.domain.community.mapper;

import org.devridge.api.domain.community.entity.CommunityComment;
import org.devridge.api.domain.community.entity.CommunityCommentLikeDislike;
import org.devridge.api.domain.community.entity.LikeStatus;
import org.devridge.api.domain.member.entity.Member;
import org.springframework.stereotype.Component;

@Component
public class CommunityCommentLikeDislikeMapper {

    public CommunityCommentLikeDislike toCommunityCommentLikeDislike(
        Member member,
        CommunityComment comment,
        LikeStatus status
    ) {
        return CommunityCommentLikeDislike.builder()
            .communityComment(comment)
            .member(member)
            .status(status)
            .build();
    }
}
