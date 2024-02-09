package org.devridge.api.domain.community.service;

import lombok.RequiredArgsConstructor;

import org.devridge.api.domain.community.entity.Community;
import org.devridge.api.domain.community.entity.CommunityScrap;
import org.devridge.api.domain.community.entity.id.CommunityScrapId;
import org.devridge.api.domain.community.repository.CommunityRepository;
import org.devridge.api.domain.community.repository.CommunityScrapRepository;
import org.devridge.api.domain.member.entity.Member;
import org.devridge.api.domain.member.repository.MemberRepository;
import org.devridge.api.exception.common.ConflictException;
import org.devridge.api.exception.common.DataNotFoundException;
import org.devridge.api.util.SecurityContextHolderUtil;

import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CommunityScrapService {

    private final CommunityScrapRepository communityScrapRepository;
    private final MemberRepository memberRepository;
    private final CommunityRepository communityRepository;

    public void createScrap(Long communityId) {
        Long writeMemberId = SecurityContextHolderUtil.getMemberId();
        Member member = getMemberById(writeMemberId);
        Community community = getCommunityById(communityId);
        CommunityScrapId communityScrapId = new CommunityScrapId(writeMemberId, communityId);

        communityScrapRepository.findById(communityScrapId)
            .ifPresent(cs -> { throw new ConflictException(); });

        if (communityScrapRepository.findById(communityScrapId).isEmpty()) { // 없나확인
            Long countResult = communityScrapRepository.checkSoftDelete(communityId, writeMemberId); // 0이외의 값은 가짜없음
            boolean isSoftDeleted = countResult != 0L;

            //가짜없음
            //reCreateScrap쿼리하기
            if (isSoftDeleted) {  // 참이면 가짜없음임 즉 실행되면
                communityScrapRepository.reCreateScrap(communityId, writeMemberId);
            }

            //  진짜없음
            //  새로만들기
            if (!isSoftDeleted) {
                CommunityScrap communityScrap = CommunityScrap.builder()
                    .community(community)
                    .member(member)
                    .build();
                communityScrapRepository.save(communityScrap);
            }
        }
    }

    public void deleteScrap(Long communityId) {
        Long writeMemberId = SecurityContextHolderUtil.getMemberId();
        Member member = getMemberById(writeMemberId);
        getCommunityById(communityId);

        CommunityScrapId communityScrapId = new CommunityScrapId(writeMemberId, communityId);
        communityScrapRepository.findById(communityScrapId)
            .orElseThrow(() -> new DataNotFoundException());
        communityScrapRepository.deleteById(communityScrapId);
    }

    private Member getMemberById(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(() -> new DataNotFoundException());
    }

    private Community getCommunityById(Long communityId) {
        return communityRepository.findById(communityId).orElseThrow(() -> new DataNotFoundException());
    }
}
