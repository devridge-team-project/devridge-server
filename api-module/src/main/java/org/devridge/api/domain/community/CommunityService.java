package org.devridge.api.domain.community;

import java.util.List;
import java.util.Optional;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommunityService {

    private CommunityRepository communityRepository;

    @Autowired
    public CommunityService(CommunityRepository communityRepository) {
        this.communityRepository = communityRepository;
    }

    public void createCommunity(CreateCommunityRequest dto, Long memberId) {
        Community community = Community.builder()
            .title(dto.getTitle())
            .content(dto.getContent())
            .memberId(memberId)
            .build();
        communityRepository.save(community);
    }

    @Transactional
    public Community getCommunityById(Long id) {
        updateView(id);
        return communityRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("해당 엔터티를 찾을 수 없습니다."));
    }

    public void updateCommunity(Long id, CreateCommunityRequest dto) {
        Optional<Community> optionalCommunity = communityRepository.findById(id);
        optionalCommunity.ifPresentOrElse(
            community -> {
                community.updateCommunity(dto.getTitle(), dto.getContent());
                communityRepository.save(community);
            },
            () -> {
                throw new EntityNotFoundException("해당 엔티티를 찾을 수 없습니다.");
            });
    }

    public void deleteCommunity(Long id) {
        try {
            communityRepository.deleteById(id);
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException("해당 엔티티를 찾을 수 없습니다.");
        }
    }

    public List<Community> viewAllCommunity() {
        List<Community> communities = communityRepository.findAll();
        if (communities.isEmpty()) {
            throw new EntityNotFoundException("해당 엔티티를 찾을 수 없습니다.");
        }
        return communities;
    }

    public void updateView(Long id) {
        communityRepository.updateView(id);
    }
}
