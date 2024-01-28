package org.devridge.api.domain.community.controller;

import lombok.RequiredArgsConstructor;
import org.devridge.api.domain.community.service.CommunityLikeDislikeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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
    public ResponseEntity createCommunityLike(@PathVariable Long communityId) {
        communityLikeDislikeService.createCommunityLike(communityId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/dislike")
    public ResponseEntity createCommunityDislike(@PathVariable Long communityId) {
        communityLikeDislikeService.createCommunityDislike(communityId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/like")
    public ResponseEntity deleteCommunityLike(@PathVariable Long communityId) {
        communityLikeDislikeService.deleteCommunityLike(communityId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/dislike")
    public ResponseEntity deleteCommunityDislike(@PathVariable Long communityId) {
        communityLikeDislikeService.deleteCommunityDislike(communityId);
        return ResponseEntity.ok().build();
    }
}
