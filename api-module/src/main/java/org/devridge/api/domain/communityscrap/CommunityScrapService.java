package org.devridge.api.domain.communityscrap;

import org.devridge.api.domain.community.Community;
import org.devridge.api.githubsociallogintemp.domain.Member;
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

    public void createScrap(Long memberId, Long communityId) {
        try {
            Member member = Member.builder().id(memberId).build();
            Community community = Community.builder().id(communityId).build();
            CommunityScrap communityScrap = CommunityScrap.builder()
                .id(new CommunityScrapId(memberId, communityId))
                .member(member)
                .community(community)
                .build();
            communityScrapRepository.save(communityScrap);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("이미 존재하는 데이터입니다.");
        }

    }

    public void deleteScrap(Long memberId, Long communityId) {
        try {
            communityScrapRepository.deleteById(new CommunityScrapId(memberId, communityId));
        } catch (EmptyResultDataAccessException e) {
            throw new EmptyResultDataAccessException("스크랩이 존재하지 않습니다.", 1);
        }
    }
}
