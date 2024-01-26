package org.devridge.api.domain.community.controller;

import java.net.URI;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.devridge.api.domain.community.dto.request.CreateCommunityRequest;
import org.devridge.api.domain.community.dto.response.CommunityDetailResponse;
import org.devridge.api.domain.community.service.CommunityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/community")
@RestController
public class CommunityController {

    private final CommunityService communityService;

    @PostMapping
    public ResponseEntity<Void> createCommunity(@Valid @RequestBody CreateCommunityRequest communityRequest) {
        Long communityId = communityService.createCommunity(communityRequest);
        return ResponseEntity.created(URI.create("/api/community/" + communityId)).build();
    }

    @GetMapping("/{communityId}")
    public ResponseEntity<CommunityDetailResponse> getCommunity(@PathVariable Long communityId) {
        CommunityDetailResponse communityDetailResponse = communityService.getCommunity(communityId);
        return ResponseEntity.ok().body(communityDetailResponse);
    }

    @PutMapping("/{communityId}")
    public ResponseEntity<Void> updateCommunity(
        @PathVariable Long communityId,
        @Valid @RequestBody CreateCommunityRequest communityRequest
    ) {
        communityService.updateCommunity(communityId, communityRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{communityId}")
    public ResponseEntity<Void> deleteCommunity(@PathVariable Long communityId) {
        communityService.deleteCommunity(communityId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/all") // 커뮤니티 글 전체 목록 보여주기
    public ResponseEntity<List<CommunityDetailResponse>> getAllCommunity() {
        List<CommunityDetailResponse> communityDetailResponses = communityService.getAllCommunity();
        return ResponseEntity.ok().body(communityDetailResponses);
    }
}

