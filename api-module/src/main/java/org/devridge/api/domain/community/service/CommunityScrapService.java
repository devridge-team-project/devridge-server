package org.devridge.api.domain.community.service;

import javax.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.devridge.api.domain.community.entity.Community;
import org.devridge.api.domain.community.entity.CommunityScrap;
import org.devridge.api.domain.community.entity.id.CommunityScrapId;
import org.devridge.api.domain.community.repository.CommunityScrapRepository;
import org.devridge.api.domain.member.entity.Member;
import org.devridge.api.util.SecurityContextHolderUtil;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CommunityScrapService {

    private final CommunityScrapRepository communityScrapRepository;

    public void createScrap(Long communityId) {
        try {
            Member member = SecurityContextHolderUtil.getMember();
            Community community = Community.builder().id(communityId).build();
            CommunityScrap communityScrap = CommunityScrap.builder()
                .id(new CommunityScrapId(member.getId(), communityId))
                .member(member)
                .community(community)
                .build();
            communityScrapRepository.save(communityScrap);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("이미 존재하는 데이터입니다.");
        }

    }

    public void deleteScrap(Long communityId) {
        Long memberId = SecurityContextHolderUtil.getMemberId();
        CommunityScrapId communityScrapId = new CommunityScrapId(memberId, communityId);
        communityScrapRepository.findById(communityScrapId)
            .orElseThrow(() -> new EntityNotFoundException("스크랩이 존재하지 않습니다."));
        communityScrapRepository.deleteById(communityScrapId);
    }
}
