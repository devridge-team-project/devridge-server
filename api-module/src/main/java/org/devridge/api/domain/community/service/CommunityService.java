package org.devridge.api.domain.community.service;

import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.devridge.api.domain.community.dto.request.CreateCommunityRequest;
import org.devridge.api.domain.community.dto.response.CommunityDetailResponse;
import org.devridge.api.domain.community.dto.response.CommunityListResponse;
import org.devridge.api.domain.community.entity.Community;
import org.devridge.api.domain.community.entity.CommunityHashtag;
import org.devridge.api.domain.community.entity.Hashtag;
import org.devridge.api.domain.community.entity.id.CommunityHashtagId;
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

@RequiredArgsConstructor
@Service
public class CommunityService {

    private final CommunityRepository communityRepository;
    private final CommunityMapper communityMapper;
    private final MemberRepository memberRepository;
    private final CommunityHashtagRepository communityHashtagRepository;
    private final HashtagRepository hashtagRepository;

    public Long createCommunity(CreateCommunityRequest communityRequest) {
        Long accessMemberId = SecurityContextHolderUtil.getMemberId();
        Member member = getMemberById(accessMemberId);
        Community community = communityMapper.toCommunity(member, communityRequest);
        return communityRepository.save(community).getId();
    }

    @Transactional
    public CommunityDetailResponse getCommunity(Long communityId) {
        updateView(communityId);
        Community community = getCommunityById(communityId);
        return communityMapper.toCommunityDetailResponse(community);
    }

    public void updateCommunity(Long communityId, CreateCommunityRequest communityRequest) {
        Long accessMemberId = SecurityContextHolderUtil.getMemberId();
        getMemberById(accessMemberId);
        Community community = getCommunityById(communityId);

        if (!accessMemberId.equals(community.getMember().getId())) {
            throw new AccessDeniedException("거부된 접근입니다.");
        }

        community.updateCommunity(communityRequest.getTitle(), communityRequest.getContent());
        communityRepository.save(community);
    }

    public void deleteCommunity(Long communityId) {
        Long accessMemberId = SecurityContextHolderUtil.getMemberId();
        Community community = getCommunityById(communityId);

        if (!accessMemberId.equals(community.getMember().getId())) {
            throw new AccessDeniedException("거부된 접근입니다.");
        }

        List<CommunityHashtag> communityHashtags = communityHashtagRepository.findAllByCommunityId(communityId); // 삭제전 communityHashtag
        communityHashtagRepository.deleteByCommunityId(communityId);
        updateByHashtagIds(communityHashtags);

        communityRepository.deleteById(communityId);
    }

    public List<CommunityListResponse> getAllCommunity() {
        List<Community> communities = communityRepository.findAll();
        return communityMapper.toCommunityListResponses(communities);
    }

    private void updateView(Long id) {
        communityRepository.updateView(id);
    }

    private Community getCommunityById(Long communityId) {
        return communityRepository.findById(communityId).orElseThrow(() -> new EntityNotFoundException());
    }

    private Member getMemberById(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(() -> new EntityNotFoundException());
    }

    @Transactional
    public Long createCommunityAndHashtag(CreateCommunityRequest communityRequest) {
        Long communityId = createCommunity(communityRequest);
        createHashtag(communityId, communityRequest.getHashtags());
        return communityId;
    }

    @Transactional
    public void updateCommunityAndHashtag(Long communityId, CreateCommunityRequest communityRequest) {
        updateCommunity(communityId, communityRequest);
        createHashtag(communityId, communityRequest.getHashtags());
    }

    @Transactional
    public void createHashtag(Long communityId, List<String> words) {
        Long accessMemberId = SecurityContextHolderUtil.getMemberId();
        Member member = getMemberById(accessMemberId);
        Community community = getCommunityById(communityId);

        if (!accessMemberId.equals(community.getMember().getId())) {
            throw new AccessDeniedException("거부된 접근입니다.");
        }

        List<CommunityHashtag> communityHashtags = communityHashtagRepository.findAllByCommunityId(communityId); // 삭제전 communityHashtag

        communityHashtagRepository.deleteByCommunityId(communityId); // 수정을 위해 softDelete

        for (String word : words) {
            Hashtag hashtag = validateHashtagAndSave(word);  // hashtag 있는지 확인후 반환, 없으면 저장 후 반환
            CommunityHashtag communityHashtag = saveOrRestoreCommunityHashtag(community, hashtag);
            communityHashtags.add(communityHashtag);
        }

        updateByHashtagIds(communityHashtags);
    }

    public void updateByHashtagIds(List<CommunityHashtag> communityHashtags) {
        List<Long> hashtagIds = deduplicationCommunityHashtags(communityHashtags);  // todo: hashtagId로 업데이트하면될듯  <- 변경된 부분에 대한 아이디임 생성 수정 삭제 모두 포함

        for (Long hashtagId : hashtagIds) {
            hashtagRepository.updateCountByHashtagId(hashtagId);
        }
    }

    private List<Long> deduplicationCommunityHashtags(List<CommunityHashtag> communityHashtags) {
        return communityHashtags.stream()
            .mapToLong(
                result -> result.getId().getHashtagId()
            ).boxed().distinct()
            .collect(Collectors.toList());
    }

    private CommunityHashtag saveOrRestoreCommunityHashtag(Community community, Hashtag hashtag) {
        return communityHashtagRepository.findByCommunityIdAndHashtagId(community.getId(), hashtag.getId())
            .map(result -> {
                communityHashtagRepository.restoreByCommunityIdAndHashtagId(community.getId(), hashtag.getId());
                return communityHashtagRepository.findById(new CommunityHashtagId(community.getId(), hashtag.getId())).orElseThrow();
            })
            .orElseGet(() -> communityHashtagRepository.save(new CommunityHashtag(community, hashtag)));
    }

    private Hashtag validateHashtagAndSave(String word) { // hashtag 검증 및 저장
        return hashtagRepository.findByWord(word).orElseGet(() -> hashtagRepository.save(new Hashtag(word)));
    }
}
