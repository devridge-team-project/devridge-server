package org.devridge.api.domain.community.controller;

import lombok.RequiredArgsConstructor;
import org.devridge.api.domain.community.service.CommunityScrapService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/community/{communityId}/scraps")
@RestController
public class CommunityScrapController {

    private final CommunityScrapService communityScrapService;

    @PostMapping
    public ResponseEntity<Void> createScrap(@PathVariable Long communityId) {
        communityScrapService.createScrap(communityId);
        return ResponseEntity.ok().build();
    }
}
