package org.devridge.api.domain.community.mapper;

import org.devridge.api.domain.community.dto.request.CreateCommunityRequest;
import org.devridge.api.domain.community.dto.response.CommunityDetailResponse;
import org.devridge.api.domain.community.dto.response.CommunityListResponse;
import org.devridge.api.domain.community.entity.Community;
import org.devridge.api.domain.member.entity.Member;
import org.springframework.stereotype.Component;

@Component
public class CommunityMapper {

    public CommunityDetailResponse toCommunityDetailResponse(Community community) {
        Member member = community.getMember();
        return CommunityDetailResponse.builder()
                .nickName(member.getNickname())
                .introduction(member.getIntroduction())
                .title(community.getTitle())
                .content(community.getContent())
                .likeCount(community.getLikeCount())
                .dislikeCount(community.getDislikeCount())
                .views(community.getViews())
                .profileImageUrl(member.getProfileImageUrl())
                .createdAt(community.getCreatedAt())
                .updatedAt(community.getUpdatedAt())
                .build();
    }

    public CommunityListResponse toCommunityListResponse(Community community, int count) {
        return CommunityListResponse.builder()
                .title(community.getTitle())
                .commentCount(Long.valueOf(count))
                .views(community.getViews())
                .likeCount(community.getLikeCount())
                .build();
    }

    public Community toCommunity(Member member, CreateCommunityRequest communityRequest) {
        return Community.builder()
            .title(communityRequest.getTitle())
            .content(communityRequest.getContent())
            .member(member)
            .build();
    }


}
