package org.devridge.api.domain.community.service;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.devridge.api.domain.community.dto.request.CreateCommunityRequest;
import org.devridge.api.domain.community.dto.response.CommunityDetailResponse;
import org.devridge.api.domain.community.dto.response.CommunityListResponse;
import org.devridge.api.domain.community.entity.Community;
import org.devridge.api.domain.community.mapper.CommunityMapper;
import org.devridge.api.domain.community.repository.CommunityCommentRepository;
import org.devridge.api.domain.community.repository.CommunityRepository;
import org.devridge.api.domain.member.entity.Member;
import org.devridge.api.domain.member.repository.MemberRepository;
import org.devridge.api.util.SecurityContextHolderUtil;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CommunityService {

    private final CommunityRepository communityRepository;
    private final CommunityMapper communityMapper;
    private final MemberRepository memberRepository;
    private final CommunityCommentRepository communityCommentRepository;


    public Long createCommunity(CreateCommunityRequest communityRequest) {
        Long writeMemberId = SecurityContextHolderUtil.getMemberId();
        Member member = getMemberById(writeMemberId);
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
        Long writeMemberId = SecurityContextHolderUtil.getMemberId();
        getMemberById(writeMemberId);
        Community community = getCommunityById(communityId);

        if (!writeMemberId.equals(community.getMember().getId())) {
            throw new AccessDeniedException("거부된 접근입니다.");
        }

        community.updateCommunity(communityRequest.getTitle(), communityRequest.getContent());
        communityRepository.save(community);
    }

    public void deleteCommunity(Long communityId) {
        Long writeMemberId = SecurityContextHolderUtil.getMemberId();
        Community community = getCommunityById(communityId);

        if (!writeMemberId.equals(community.getMember().getId())) {
            throw new AccessDeniedException("거부된 접근입니다.");
        }

        communityRepository.deleteById(communityId);
    }

    public List<CommunityListResponse> getAllCommunity() {
        List<Community> communities = communityRepository.findAll();

        if (communities.isEmpty()) {
            throw new EntityNotFoundException("해당 엔티티를 찾을 수 없습니다.");
        }

        List<CommunityListResponse> communityListResponses = new ArrayList<>();

        communities.forEach(
            result -> {
                int count = communityCommentRepository.countByCommunityId(result.getId());
                communityListResponses.add(communityMapper.toCommunityListResponse(result, count));
            }
        );

        return communityListResponses;
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
}
