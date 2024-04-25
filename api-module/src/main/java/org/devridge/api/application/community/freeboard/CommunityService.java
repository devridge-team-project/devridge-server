package org.devridge.api.application.community.freeboard;

import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.devridge.api.application.s3.S3Service;
import org.devridge.api.common.exception.common.DataNotFoundException;
import org.devridge.api.common.util.SecurityContextHolderUtil;
import org.devridge.api.domain.community.dto.request.CreateCommunityRequest;
import org.devridge.api.domain.community.dto.response.CommunityDetailResponse;
import org.devridge.api.domain.community.dto.response.CommunitySliceResponse;
import org.devridge.api.domain.community.entity.Community;
import org.devridge.api.domain.community.exception.MyCommunityForbiddenException;
import org.devridge.api.domain.member.entity.Member;
import org.devridge.api.infrastructure.community.freeboard.CommunityQuerydslRepository;
import org.devridge.api.infrastructure.community.freeboard.CommunityRepository;
import org.devridge.api.infrastructure.member.MemberRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CommunityService {

    private final CommunityRepository communityRepository;
    private final CommunityMapper communityMapper;
    private final MemberRepository memberRepository;
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

        communityRepository.deleteById(communityId);

        if (community.getImages() != null) {
            List<String> images = Arrays.asList(community.getImages().split(", "));
            s3Service.deleteAllImage(images);
        }
    }

    public List<CommunitySliceResponse> getAllCommunity(Long lastId, Pageable pageable) {
        return communityQuerydslRepository.searchByCommunity(lastId, pageable);
    }

    private Community getCommunityById(Long communityId) {
        return communityRepository.findById(communityId).orElseThrow(() -> new DataNotFoundException());
    }

    private Member getMemberById(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(() -> new DataNotFoundException());
    }
}