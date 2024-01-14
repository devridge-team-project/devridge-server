package org.devridge.api.domain.community;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class CommunityController {

    private CommunityService communityService;

    @Autowired
    public CommunityController(CommunityService communityService) {
        this.communityService = communityService;
    }

    @PostMapping("/community/write/{memberId}")
    public ResponseEntity<?> writingCommunity(@RequestBody CreateCommunityRequest dto,
        @PathVariable Long memberId) {
        Community community = communityService.createCommunity(dto, memberId);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/community/read/{id}")
    public ResponseEntity<?> viewCommunity(@PathVariable Long id) {
        Optional<Community> community = communityService.getCommunityById(id);
        if (community.isPresent()) {
            CommunityDto dto = new CommunityDto(community.get());
            return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/community/modify/{id}")
    public ResponseEntity<?> modifyCommunity(@PathVariable Long id, @RequestBody CommunityDto dto) {
        Community community = communityService.updateCommunity(id, dto);
        log.info("title={}, content={}", dto.getTitle(), dto.getContent());
        CommunityDto dto1 = new CommunityDto(community);
        return ResponseEntity.ok(dto1);
    }

    @DeleteMapping("/community/del/{id}")
    public ResponseEntity<?> deleteCommunity(@PathVariable Long id) {
        Community community = communityService.deleteCommunity(id);
        CommunityDto dto = new CommunityDto(community);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/community/read/all")
    public ResponseEntity<?> viewAll() {
        List<Community> communityList = communityService.viewAllCommunity();
        List<CommunityDto> dtos = communityList.stream()
            .map(community -> {
                CommunityDto dto = new CommunityDto(community);
                return dto;
            })
            .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }
}

