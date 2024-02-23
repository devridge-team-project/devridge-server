package org.devridge.api.domain.community.service;

import lombok.RequiredArgsConstructor;
import org.devridge.api.domain.community.entity.Community;
import org.devridge.api.domain.community.entity.CommunityScrap;
import org.devridge.api.domain.community.entity.id.CommunityScrapId;
import org.devridge.api.domain.community.exception.MyCommunityForbiddenException;
import org.devridge.api.domain.community.mapper.CommunityScrapMapper;
import org.devridge.api.domain.community.repository.CommunityRepository;
import org.devridge.api.domain.community.repository.CommunityScrapRepository;
import org.devridge.api.domain.member.entity.Member;
import org.devridge.api.domain.member.repository.MemberRepository;
import org.devridge.api.exception.common.DataNotFoundException;
import org.devridge.api.util.SecurityContextHolderUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CommunityScrapService {

    private final CommunityScrapRepository communityScrapRepository;
    private final MemberRepository memberRepository;
    private final CommunityRepository communityRepository;
    private final CommunityScrapMapper communityScrapMapper;

    @Transactional
    public void createScrap(Long communityId) {
        Long accessMemberId = SecurityContextHolderUtil.getMemberId();
        Member member = getMemberById(accessMemberId);
        Community community = getCommunityById(communityId);
        CommunityScrapId communityScrapId = new CommunityScrapId(accessMemberId, communityId);

        if (accessMemberId.equals(community.getMember().getId())) {
            throw new MyCommunityForbiddenException(403, "내가 작성한 글은 스크랩할 수 없습니다.");
        }

        communityScrapRepository.findById(communityScrapId).ifPresentOrElse(
            result -> {
                if (result.getIsDeleted()) {
                    communityScrapRepository.restoreById(communityScrapId);
                }

                if (!result.getIsDeleted()) {
                    communityScrapRepository.deleteById(communityScrapId);
                }
            },
            () -> {
                CommunityScrap communityScrap = communityScrapMapper.toCommunityScrap(community, member);
                communityScrapRepository.save(communityScrap);
            }
        );
    }

    private Member getMemberById(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(() -> new DataNotFoundException());
    }

    private Community getCommunityById(Long communityId) {
        return communityRepository.findById(communityId).orElseThrow(() -> new DataNotFoundException());
    }

}