package org.devridge.api.domain.community.controller;

import lombok.RequiredArgsConstructor;
import org.devridge.api.domain.community.service.CommunityCommentLikeDislikeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/community/{communityId}/comments/{commentId}")
@RestController
public class CommunityCommentLikeDislikeController {

    private final CommunityCommentLikeDislikeService communityCommentLikeDislikeService;

    @PostMapping("/like")
    public ResponseEntity<Void> createCommunityCommentLike(
        @PathVariable Long communityId,
        @PathVariable Long commentId
    ) {
        communityCommentLikeDislikeService.createCommunityCommentLike(communityId, commentId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/dislike")
    public ResponseEntity<Void> createCommunityCommentDislike(
        @PathVariable Long communityId,
        @PathVariable Long commentId
        ) {
        communityCommentLikeDislikeService.createCommunityCommentDislike(communityId, commentId);
        return ResponseEntity.ok().build();
    }


}
