package org.devridge.api.application.community.freeboard;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.devridge.api.common.dto.UserInformation;
import org.devridge.api.domain.community.dto.request.CreateCommunityRequest;
import org.devridge.api.domain.community.dto.response.CommunityDetailResponse;
import org.devridge.api.domain.community.dto.response.CommunityListResponse;
import org.devridge.api.domain.community.entity.Community;
import org.devridge.api.domain.member.entity.Member;

import org.springframework.stereotype.Component;

import static org.devridge.api.common.util.MemberUtil.toMember;

@Component
public class CommunityMapper {

    public CommunityDetailResponse toCommunityDetailResponse(Community community) {
        UserInformation memberInfoResponse = toMember(community.getMember());

        return CommunityDetailResponse.builder()
                .communityId(community.getId())
                .userInformation(memberInfoResponse)
                .title(community.getTitle())
                .content(community.getContent())
                .likeCount(community.getLikeCount())
                .dislikeCount(community.getDislikeCount())
                .viewCount(community.getViewCount() + 1)
                .createdAt(community.getCreatedAt())
                .updatedAt(community.getUpdatedAt())
                .hashtags(toHashtags(community))
                .build();
    }

    public List<String> toHashtags(Community community) {
        return community.getHashtags()
                .stream()
                .map(result -> result.getHashtag().getWord())
                .distinct()
                .collect(Collectors.toList());
    }

    public CommunityListResponse toCommunityListResponse(Community community) {
        return CommunityListResponse.builder()
                .id(community.getId())
                .title(community.getTitle())
                .commentCount(Long.valueOf(community.getComments().size()))
                .viewCount(community.getViewCount())
                .likeCount(community.getLikeCount())
                .build();
    }

    public List<CommunityListResponse> toCommunityListResponses(List<Community> communities) {
        List<CommunityListResponse> communityListResponses = new ArrayList<>();
        communities.forEach(
            result -> {
                communityListResponses.add(toCommunityListResponse(result));
            }
        );
        return communityListResponses;
    }

    public Community toCommunity(Member member, CreateCommunityRequest communityRequest) {
        if (communityRequest.getImages() != null && !communityRequest.getImages().isEmpty()) {
            String images = communityRequest.getImages().toString();
            return Community.builder()
                .title(communityRequest.getTitle())
                .content(communityRequest.getContent())
                .member(member)
                .images(images.substring(1, images.length() - 1))
                .build();
        }

        return Community.builder()
                .title(communityRequest.getTitle())
                .content(communityRequest.getContent())
                .member(member)
                .build();
    }
}
