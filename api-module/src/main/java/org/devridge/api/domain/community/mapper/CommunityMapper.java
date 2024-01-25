package org.devridge.api.domain.community.mapper;

import java.util.ArrayList;
import java.util.List;
import org.devridge.api.domain.community.dto.request.CreateCommunityRequest;
import org.devridge.api.domain.community.dto.response.CommunityDetailResponse;
import org.devridge.api.domain.community.entity.Community;
import org.devridge.api.domain.member.entity.Member;
import org.springframework.stereotype.Component;

@Component
public class CommunityMapper {

    public CommunityDetailResponse toCommunityDetailResponse(Community community) {
        String nickName = community.getMember().getNickname();
        return CommunityDetailResponse.builder()
            .nickName(nickName)
            .title(community.getTitle())
            .content(community.getContent())
            .views(community.getViews())
            .createdAt(community.getCreatedAt())
            .updatedAt(community.getUpdatedAt())
            .build();
    }

    public List<CommunityDetailResponse> toCommunityDetailResponses(List<Community> communityList) {
        List<CommunityDetailResponse> communityDetailResponses = new ArrayList<>();
        for (Community community : communityList) {
            communityDetailResponses.add(
                toCommunityDetailResponse(community)
            );
        }
        return communityDetailResponses;
    }

    public Community toCommunity(Member member, CreateCommunityRequest communityRequest) {
        return Community.builder()
            .title(communityRequest.getTitle())
            .content(communityRequest.getContent())
            .member(member)
            .build();
    }


}
