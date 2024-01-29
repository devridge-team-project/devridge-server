package org.devridge.api.domain.community.mapper;

import java.util.ArrayList;
import java.util.List;
import org.devridge.api.domain.community.dto.request.CommunityCommentRequest;
import org.devridge.api.domain.community.dto.response.CommunityCommentResponse;
import org.devridge.api.domain.community.entity.Community;
import org.devridge.api.domain.community.entity.CommunityComment;
import org.devridge.api.domain.member.entity.Member;
import org.springframework.stereotype.Component;

@Component
public class CommunityCommentMapper {

    public List<CommunityCommentResponse> toCommentResponses(List<CommunityComment> communityComments) {
        List<CommunityCommentResponse> commentResponses = new ArrayList<>();
        for (CommunityComment comment : communityComments) {
            Member member = comment.getMember();
            commentResponses.add(
                CommunityCommentResponse.builder()
                    .nickName(member.getNickname())
                    .introduction(member.getIntroduction())
                    .profileImageUrl(member.getProfileImageUrl())
                    .likeCount(comment.getLikeCount())
                    .dislikeCount(comment.getDislikeCount())
                    .createdAt(comment.getCreatedAt())
                    .updatedAt(comment.getUpdatedAt())
                    .content(comment.getContent())
                    .build()
            );
        }
        return commentResponses;
    }

    public CommunityComment toCommunityComment(Community community, Member member, CommunityCommentRequest Request) {
        return CommunityComment.builder()
            .community(community)
            .content(Request.getContent())
            .member(member)
            .build();
    }
}
