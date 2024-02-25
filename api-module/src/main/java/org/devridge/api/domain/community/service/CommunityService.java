package org.devridge.api.domain.community.service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.devridge.api.domain.community.dto.request.CreateCommunityRequest;
import org.devridge.api.domain.community.dto.response.CommunityDetailResponse;
import org.devridge.api.domain.community.dto.response.CommunityListResponse;
import org.devridge.api.domain.community.entity.Community;
import org.devridge.api.domain.community.entity.CommunityHashtag;
import org.devridge.api.domain.community.entity.Hashtag;
import org.devridge.api.domain.community.entity.id.CommunityHashtagId;
import org.devridge.api.domain.community.exception.MyCommunityForbiddenException;
import org.devridge.api.domain.community.mapper.CommunityMapper;
import org.devridge.api.domain.community.repository.CommunityHashtagRepository;
import org.devridge.api.domain.community.repository.CommunityQuerydslRepository;
import org.devridge.api.domain.community.repository.CommunityRepository;
import org.devridge.api.domain.community.repository.HashtagRepository;
import org.devridge.api.domain.member.entity.Member;
import org.devridge.api.domain.member.repository.MemberRepository;
import org.devridge.api.domain.s3.service.S3Service;
import org.devridge.api.exception.common.DataNotFoundException;
import org.devridge.api.util.SecurityContextHolderUtil;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
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
    private final S3Service s3Service;
    private final CommunityQuerydslRepository communityQuerydslRepository;

    public Long createCommunity(CreateCommunityRequest communityRequest) {
        Long accessMemberId = SecurityContextHolderUtil.getMemberId();
        Member member = getMemberById(accessMemberId);
        Community community = communityMapper.toCommunity(member, communityRequest);
        return communityRepository.save(community).getId();
    }

    @Transactional
    public CommunityDetailResponse getCommunity(Long communityId) {
        Community community = getCommunityById(communityId);
        communityRepository.updateView(communityId);
        return communityMapper.toCommunityDetailResponse(community);
    }

    public void updateCommunity(Long communityId, CreateCommunityRequest communityRequest) {
        Long accessMemberId = SecurityContextHolderUtil.getMemberId();
        getMemberById(accessMemberId);
        Community community = getCommunityById(communityId);

        if (!accessMemberId.equals(community.getMember().getId())) {
            throw new MyCommunityForbiddenException(403, "내가 작성하지 않은 글은 수정할 수 없습니다.");
        }

        community.updateCommunity(communityRequest.getTitle(), communityRequest.getContent());
        communityRepository.save(community);
    }

    @Transactional
    public void deleteCommunity(Long communityId) {
        Long accessMemberId = SecurityContextHolderUtil.getMemberId();
        Community community = getCommunityById(communityId);

        if (!accessMemberId.equals(community.getMember().getId())) {
            throw new MyCommunityForbiddenException(403, "내가 작성하지 않은 글은 삭제할 수 없습니다.");
        }

        List<CommunityHashtag> communityHashtags = communityHashtagRepository.findAllByCommunityId(communityId); // 삭제전 communityHashtag
        communityHashtagRepository.deleteByCommunityId(communityId);
        updateByHashtagIds(communityHashtags);

        communityRepository.deleteById(communityId);
        if (community.getImages() != null) {
            List<String> images = Arrays.asList(community.getImages().split(", "));
            s3Service.deleteAllImage(images);
        }
    }

//    public List<CommunityListResponse> getAllCommunity() {
//        List<Community> communities = communityRepository.findAll();
//        return communityMapper.toCommunityListResponses(communities);
//    }

    public Slice<CommunityListResponse> getAllCommunity(Long lastId, Pageable pageable) {
        return communityQuerydslRepository.searchBySlice(lastId, pageable);
    }

    private Community getCommunityById(Long communityId) {
        return communityRepository.findById(communityId).orElseThrow(() -> new DataNotFoundException());
    }

    private Member getMemberById(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(() -> new DataNotFoundException());
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
            throw new MyCommunityForbiddenException(403, "내가 작성하지 않은 글은 수정할 수 없습니다.");
        }

        List<CommunityHashtag> communityHashtags = communityHashtagRepository.findAllByCommunityId(communityId);

        communityHashtagRepository.deleteByCommunityId(communityId);

        for (String word : words) {
            Hashtag hashtag = validateHashtagAndSave(word);
            CommunityHashtag communityHashtag = saveOrRestoreCommunityHashtag(community, hashtag);
            communityHashtags.add(communityHashtag);
        }

        updateByHashtagIds(communityHashtags);
    }

    public void updateByHashtagIds(List<CommunityHashtag> communityHashtags) {
        List<Long> hashtagIds = deduplicationCommunityHashtags(communityHashtags);

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

    private CommunityHashtag saveOrRestoreCommunityHashtag(Community community,
        Hashtag hashtag) {  // 소프트 딜리트 포함 가져오기  -> 다 지워진상태or 없는상태 위에서 다지움
        return communityHashtagRepository.findByCommunityIdAndHashtagId(community.getId(), hashtag.getId())
            .map(result -> {
                communityHashtagRepository.restoreByCommunityIdAndHashtagId(community.getId(), hashtag.getId());
                return communityHashtagRepository.findById(new CommunityHashtagId(community.getId(), hashtag.getId()))
                    .orElseThrow();
            })
            .orElseGet(() -> communityHashtagRepository.save(new CommunityHashtag(community, hashtag)));
    }

    private Hashtag validateHashtagAndSave(String word) {
        return hashtagRepository.findByWord(word).orElseGet(() -> hashtagRepository.save(new Hashtag(word)));
    }
}