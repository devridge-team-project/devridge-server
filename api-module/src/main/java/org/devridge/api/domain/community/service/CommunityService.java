package org.devridge.api.domain.community.service;

import java.util.List;

import javax.transaction.Transactional;

import lombok.RequiredArgsConstructor;

import org.devridge.api.domain.community.dto.request.CreateCommunityRequest;
import org.devridge.api.domain.community.dto.response.CommunityDetailResponse;
import org.devridge.api.domain.community.entity.Community;
import org.devridge.api.domain.community.mapper.CommunityMapper;
import org.devridge.api.domain.community.repository.CommunityRepository;
import org.devridge.api.domain.member.entity.Member;
import org.devridge.api.domain.member.repository.MemberRepository;
import org.devridge.api.exception.common.DataNotFoundException;
import org.devridge.api.exception.common.PutOrDeleteForbiddenException;
import org.devridge.api.util.SecurityContextHolderUtil;

import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CommunityService {

    private final CommunityRepository communityRepository;
    private final CommunityMapper communityMapper;
    private final MemberRepository memberRepository;


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
            throw new PutOrDeleteForbiddenException();
        }

        community.updateCommunity(communityRequest.getTitle(), communityRequest.getContent());
        communityRepository.save(community);
    }

    public void deleteCommunity(Long communityId) {
        Long writeMemberId = SecurityContextHolderUtil.getMemberId();
        Community community = getCommunityById(communityId);

        if (!writeMemberId.equals(community.getMember().getId())) {
            throw new PutOrDeleteForbiddenException();
        }

        communityRepository.deleteById(communityId);
    }

    public List<CommunityDetailResponse> getAllCommunity() {
        List<Community> communities = communityRepository.findAll();

        if (communities.isEmpty()) {
            throw new DataNotFoundException();
        }

        return communityMapper.toCommunityDetailResponses(communities);
    }

    private void updateView(Long id) {
        communityRepository.updateView(id);
    }

    private Community getCommunityById(Long communityId) {
        return communityRepository.findById(communityId).orElseThrow(() -> new DataNotFoundException());
    }

    private Member getMemberById(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(() -> new DataNotFoundException());
    }
}
