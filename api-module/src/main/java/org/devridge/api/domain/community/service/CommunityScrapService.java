package org.devridge.api.domain.community.service;

import org.devridge.api.domain.community.entity.Community;
import org.devridge.api.domain.community.entity.CommunityScrap;
import org.devridge.api.domain.community.repository.CommunityScrapRepository;
import org.devridge.api.domain.community.entity.id.CommunityScrapId;
import org.devridge.api.domain.member.entity.Member;
import org.devridge.api.util.SecurityContextHolderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

@Service
public class CommunityScrapService {

    private CommunityScrapRepository communityScrapRepository;

    @Autowired
    public CommunityScrapService(CommunityScrapRepository communityScrapRepository) {
        this.communityScrapRepository = communityScrapRepository;
    }

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
        try {
            communityScrapRepository.deleteById(
                new CommunityScrapId(SecurityContextHolderUtil.getMemberId(), communityId));
        } catch (EmptyResultDataAccessException e) {
            throw new EmptyResultDataAccessException("스크랩이 존재하지 않습니다.", 1);
        }
    }
}
