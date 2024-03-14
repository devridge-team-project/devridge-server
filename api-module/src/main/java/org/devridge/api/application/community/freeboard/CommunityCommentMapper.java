package org.devridge.api.application.community.freeboard;

import org.devridge.api.domain.community.dto.request.CommunityCommentRequest;
import org.devridge.api.domain.community.entity.Community;
import org.devridge.api.domain.community.entity.CommunityComment;
import org.devridge.api.domain.member.entity.Member;

import org.springframework.stereotype.Component;

@Component
public class CommunityCommentMapper {

    public CommunityComment toCommunityComment(Community community, Member member, CommunityCommentRequest Request) {
        return CommunityComment.builder()
                .community(community)
                .content(Request.getContent())
                .member(member)
                .build();
    }
}
