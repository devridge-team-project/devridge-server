package org.devridge.api.domain.communityscrap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CommunityScrapController {

    private CommunityScrapService communityScrapService;

    @Autowired
    public CommunityScrapController(CommunityScrapService communityScrapService) {
        this.communityScrapService = communityScrapService;
    }

    @PostMapping("/community/scrap/{communityId}/{memberId}")
    public ResponseEntity<?> scrapCreate(@PathVariable Long memberId,
        @PathVariable Long communityId) {
        communityScrapService.createScrap(memberId, communityId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/community/scrap/{communityId}/{memberId}")
    public ResponseEntity<?> scrapdelete(@PathVariable Long memberId,
        @PathVariable Long communityId) {
        communityScrapService.deleteScrap(memberId, communityId);
        return ResponseEntity.ok().build();
    }
}
