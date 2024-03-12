package org.devridge.api.application.community;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.devridge.api.domain.community.dto.response.CommunityListResponse;
import org.devridge.api.domain.community.entity.Community;
import org.devridge.api.domain.community.entity.CommunityHashtag;
import org.devridge.api.infrastructure.community.CommunityHashtagRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommunityHashtagService {

    private final CommunityHashtagRepository communityHashtagRepository;
    private final CommunityMapper communityMapper;

    public List<CommunityListResponse> getHashtagToCommunityList(Long hashtagId) {
        List<CommunityHashtag> communityHashtags = communityHashtagRepository.findAllByHashtagId(hashtagId);

        List<Community> communities = communityHashtags.stream()
            .map(CommunityHashtag::getCommunity)
            .collect(Collectors.toList());

        return communityMapper.toCommunityListResponses(communities);
    }
}
