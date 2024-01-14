package org.devridge.api.domain.community;

import java.util.List;
import java.util.Optional;
import javax.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommunityService {

    private CommunityRepository communityRepository;

    @Autowired
    public CommunityService(CommunityRepository communityRepository) {
        this.communityRepository = communityRepository;
    }

    public Community createCommunity(CommunityDto dto, Long MemberId) {
        Community community = Community.builder().title(dto.getTitle()).content(dto.getContent()).memberId(MemberId)
            .build();
        return communityRepository.save(community);
    }

    public Community getCommunityById(Long id) {
        return communityRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("해당 엔터티를 찾을 수 없습니다."));
    }

    public Community updateCommunity(Long id, CommunityDto dto) {
        Optional<Community> optionalCommunity = communityRepository.findById(id);
        Community community = optionalCommunity.orElseThrow(() -> new EntityNotFoundException("Community not found"));
        community.setTitle(dto.getTitle());
        community.setContent(dto.getContent());
        return communityRepository.save(community);
    }

    public Community deleteCommunity(Long id) {
        Optional<Community> optionalCommunity = communityRepository.findById(id);
        Community community = optionalCommunity.orElseThrow(() -> new EntityNotFoundException("Community not found"));
        community.setIsDeleted(true);
        return communityRepository.save(community);
    }

    public List<Community> viewAllCommunity() {
        List<Community> communities = communityRepository.findAll();
        return communities;
    }

}
