package org.devridge.api.presentation.controller.community.freeboard;

import lombok.RequiredArgsConstructor;
import org.devridge.api.application.community.freeboard.CommunityLikeDislikeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/community/{communityId}")
public class CommunityLikeDislikeController {

    private final CommunityLikeDislikeService communityLikeDislikeService;

    @PostMapping("/like")
    public ResponseEntity<Void> createCommunityLike(@PathVariable Long communityId) {
        communityLikeDislikeService.createCommunityLike(communityId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/dislike")
    public ResponseEntity<Void> createCommunityDislike(@PathVariable Long communityId) {
        communityLikeDislikeService.createCommunityDislike(communityId);
        return ResponseEntity.ok().build();
    }
}
