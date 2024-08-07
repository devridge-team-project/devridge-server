package org.devridge.api.presentation.controller.community.freeboard;

import java.net.URI;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.devridge.api.application.community.freeboard.CommunityService;
import org.devridge.api.domain.community.dto.request.CreateCommunityRequest;
import org.devridge.api.domain.community.dto.response.CommunityDetailResponse;
import org.devridge.api.domain.community.dto.response.CommunitySliceResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/community")
@RestController
public class CommunityController {

    private final CommunityService communityService;

    @PostMapping
    public ResponseEntity<Void> createCommunityAndHashtag(@Valid @RequestBody CreateCommunityRequest communityRequest) {
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

    @GetMapping
    public ResponseEntity<List<CommunitySliceResponse>> getAllCommunity(
        @RequestParam(name = "lastId", required = false) Long lastId,
        @PageableDefault Pageable pageable
    ) {
        List<CommunitySliceResponse> communitySliceResponses = communityService.getAllCommunity(lastId, pageable);
        return ResponseEntity.ok().body(communitySliceResponses);
    }
}

