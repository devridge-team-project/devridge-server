package org.devridge.api.application.community.freeboard;

import static org.devridge.api.common.util.MemberUtil.toMember;

import java.util.ArrayList;
import java.util.List;
import org.devridge.api.common.dto.UserInformation;
import org.devridge.api.domain.community.dto.request.CreateCommunityRequest;
import org.devridge.api.domain.community.dto.response.CommunityDetailResponse;
import org.devridge.api.domain.community.dto.response.CommunityListResponse;
import org.devridge.api.domain.community.entity.Community;
import org.devridge.api.domain.member.entity.Member;
import org.springframework.stereotype.Component;

@Component
public class CommunityMapper {

    public CommunityDetailResponse toCommunityDetailResponse(Community community) {
        UserInformation memberInfoResponse = toMember(community.getMember());

        return CommunityDetailResponse.builder()
                .communityId(community.getId())
                .userInformation(memberInfoResponse)
                .title(community.getTitle())
                .content(community.getContent())
                .likes(community.getLikes())
                .dislikes(community.getDislikes())
                .views(community.getViews() + 1)
                .createdAt(community.getCreatedAt())
                .updatedAt(community.getUpdatedAt())
                .scraps(Long.valueOf(community.getScraps().size()))
                .comments(Long.valueOf(community.getComments().size()))
                .build();
    }

    public CommunityListResponse toCommunityListResponse(Community community) {
        return CommunityListResponse.builder()
                .id(community.getId())
                .title(community.getTitle())
                .comments(Long.valueOf(community.getComments().size()))
                .views(community.getViews())
                .likes(community.getLikes())
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
