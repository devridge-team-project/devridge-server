package org.devridge.api.domain.community.service;

import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.devridge.api.domain.community.dto.response.CommunityListResponse;
import org.devridge.api.domain.community.entity.Community;
import org.devridge.api.domain.community.entity.CommunityHashtag;
import org.devridge.api.domain.community.entity.Hashtag;
import org.devridge.api.domain.community.mapper.CommunityMapper;
import org.devridge.api.domain.community.repository.CommunityHashtagRepository;
import org.devridge.api.domain.community.repository.CommunityRepository;
import org.devridge.api.domain.community.repository.HashtagRepository;
import org.devridge.api.domain.member.entity.Member;
import org.devridge.api.domain.member.repository.MemberRepository;
import org.devridge.api.util.SecurityContextHolderUtil;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommunityHashtagService {

    private final CommunityHashtagRepository communityHashtagRepository;
    private final CommunityRepository communityRepository;
    private final HashtagRepository hashtagRepository;
    private final MemberRepository memberRepository;
    private final CommunityMapper communityMapper;

    @Transactional
    public void createHashtag(Long communityId, List<String> words) {
        Long accessMemberId = SecurityContextHolderUtil.getMemberId();
        Member member = getMemberById(accessMemberId);
        Community community = getCommunityById(communityId);

        if (!accessMemberId.equals(community.getMember().getId())) {
            throw new AccessDeniedException("거부된 접근입니다.");
        }

        communityHashtagRepository.deleteByCommunityId(communityId);

        for (String word : words) {
            Hashtag hashtag = validateHashtagAndSave(word);         // hashtag 있는지 확인후 반환, 없으면 저장 후 반환
            communityHashtagRepository.findByCommunityIdAndHashtagId(communityId, hashtag.getId()).ifPresentOrElse( // communityHashtag 있으면 복구 없으면 저장
                result -> {
                    communityHashtagRepository.restoreByCommunityIdAndHashtagId(communityId, hashtag.getId());
                },
                () -> {
                    communityHashtagRepository.save(new CommunityHashtag(community, hashtag));
                }
            );
        }
//        hashtagRepository.updateHashtagPropertyForCommunity(communityId);  // todo: hashtag 카운트 업데이트 이후 배치??
    }

    public Hashtag validateHashtagAndSave(String word) { // hashtag 검증 및 저장
        return hashtagRepository.findByWord(word).orElseGet(() -> hashtagRepository.save(new Hashtag(word)));
    }

    public List<CommunityListResponse> getHashtagToCommunityList(Long hashtagId) { // 태그 이름을 받아서 or 나중에 id를 받아서?
        List<CommunityHashtag> communityHashtags = communityHashtagRepository.findAllByHashtagId(hashtagId); // 관련 커뮤니티 구하기

        List<Community> communities = communityHashtags.stream() // 커뮤니티로 변환
            .map(CommunityHashtag::getCommunity)
            .collect(Collectors.toList());

         return communityMapper.toCommunityListResponses(communities); // 커뮤니티 목록 dto 반환
    }

    private Member getMemberById(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(() -> new EntityNotFoundException());
    }

    private Community getCommunityById(Long communityId) {
        return communityRepository.findById(communityId).orElseThrow(() -> new EntityNotFoundException());
    }

}
