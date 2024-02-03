package org.devridge.api.domain.community.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.devridge.api.domain.community.dto.request.hashtagRequest;
import org.devridge.api.domain.community.dto.response.CommunityListResponse;
import org.devridge.api.domain.community.service.CommunityHashtagService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CommunityHashtagController {

    private final CommunityHashtagService communityHashtagService;

    @GetMapping("/api/community/{communityId}/hashtag/{hashtagId}")
    public ResponseEntity<List<CommunityListResponse>> getHashtagToCommunityList(
        @PathVariable Long communityId,
        @PathVariable Long hashtagId
        ) {
        List<CommunityListResponse> responses = communityHashtagService.getHashtagToCommunityList(hashtagId);
        return ResponseEntity.ok().body(responses);
    }
}