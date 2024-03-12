package org.devridge.api.application.community;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.devridge.api.domain.community.dto.request.CommunityCommentRequest;
import org.devridge.api.domain.community.dto.response.CommunityCommentResponse;
import org.devridge.api.domain.community.dto.response.MemberInfoResponse;
import org.devridge.api.domain.community.entity.Community;
import org.devridge.api.domain.community.entity.CommunityComment;
import org.devridge.api.domain.member.entity.Member;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommunityCommentMapper {

    private final MemberInfoMapper memberInfoMapper;

    public List<CommunityCommentResponse> toCommentResponses(List<CommunityComment> communityComments) {
        List<CommunityCommentResponse> commentResponses = new ArrayList<>();
        for (CommunityComment comment : communityComments) {
            commentResponses.add(
                toCommentResponse(comment)
            );
        }
        return commentResponses;
    }

    public CommunityCommentResponse toCommentResponse(CommunityComment comment) {
        Member member = comment.getMember();
        MemberInfoResponse memberInfoResponse = memberInfoMapper.toMemberInfoResponse(member);
        return CommunityCommentResponse.builder()
                .commentId(comment.getId())
                .memberInfoResponse(memberInfoResponse)
                .likeCount(comment.getLikeCount())
                .dislikeCount(comment.getDislikeCount())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .content(comment.getContent())
                .build();
    }

    public CommunityComment toCommunityComment(Community community, Member member, CommunityCommentRequest Request) {
        return CommunityComment.builder()
                .community(community)
                .content(Request.getContent())
                .member(member)
                .build();
    }
}
