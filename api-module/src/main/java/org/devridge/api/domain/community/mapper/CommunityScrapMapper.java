package org.devridge.api.domain.community.mapper;

import org.devridge.api.domain.community.entity.Community;
import org.devridge.api.domain.community.entity.CommunityScrap;
import org.devridge.api.domain.member.entity.Member;
import org.springframework.stereotype.Component;

@Component
public class CommunityScrapMapper {

    public CommunityScrap toCommunityScrap(Community community, Member member) {
        return CommunityScrap.builder()
            .community(community)
            .member(member)
            .build();
    }
}
