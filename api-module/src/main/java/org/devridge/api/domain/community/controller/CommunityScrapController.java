package org.devridge.api.domain.community.controller;

import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.devridge.api.domain.community.service.CommunityScrapService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/community/{communityId}/scrap")
@RestController
public class CommunityScrapController {

    private final CommunityScrapService communityScrapService;

    @PostMapping
    public ResponseEntity<Void> scrapCreate(@PathVariable Long communityId) {
        communityScrapService.createScrap(communityId);
        return ResponseEntity.created(URI.create("/api/community/" + communityId + "/scrap")).build();
    }

    @DeleteMapping
    public ResponseEntity<Void> scrapdelete(@PathVariable Long communityId) {
        communityScrapService.deleteScrap(communityId);
        return ResponseEntity.ok().build();
    }
}
