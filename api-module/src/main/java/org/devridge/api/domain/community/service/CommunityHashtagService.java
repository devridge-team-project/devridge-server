package org.devridge.api.domain.community.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.devridge.api.domain.community.dto.response.CommunityListResponse;
import org.devridge.api.domain.community.entity.Community;
import org.devridge.api.domain.community.entity.CommunityHashtag;
import org.devridge.api.domain.community.mapper.CommunityMapper;
import org.devridge.api.domain.community.repository.CommunityHashtagRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommunityHashtagService {

    private final CommunityHashtagRepository communityHashtagRepository;
    private final CommunityMapper communityMapper;

    public List<CommunityListResponse> getHashtagToCommunityList(Long hashtagId) { // 태그 이름을 받아서 or 나중에 id를 받아서?
        List<CommunityHashtag> communityHashtags = communityHashtagRepository.findAllByHashtagId(
            hashtagId); // 관련 커뮤니티 구하기

        List<Community> communities = communityHashtags.stream() // 커뮤니티로 변환
            .map(CommunityHashtag::getCommunity)
            .collect(Collectors.toList());

        return communityMapper.toCommunityListResponses(communities); // 커뮤니티 목록 dto 반환
    }
}
