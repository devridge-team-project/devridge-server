package org.devridge.api.domain.community.mapper;

import org.devridge.api.domain.community.entity.Community;
import org.devridge.api.domain.community.entity.CommunityLikeDislike;
import org.devridge.api.domain.community.entity.LikeStatus;
import org.devridge.api.domain.member.entity.Member;
import org.springframework.stereotype.Component;

@Component
public class CommunityLikeDislikeMapper {

    public CommunityLikeDislike toCommunityLikeDislike(Community community, Member member, LikeStatus status) {
        return CommunityLikeDislike.builder()
            .community(community)
            .member(member)
            .status(status)
            .build();
    }
}
