package org.devridge.api.domain.community.controller;

import javax.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.devridge.api.domain.community.entity.LikeStatus;
import org.devridge.api.domain.community.service.CommunityCommentLikeDislikeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/community/{communityId}/comment/{commentId}/like-dislike/{status}")
@RestController
public class CommunityCommentLikeDislikeController {

    private final CommunityCommentLikeDislikeService communityCommentLikeDislikeService;

    @PostMapping
    public ResponseEntity<?> likeDislikeCreate(
        @PathVariable Long communityId,
        @PathVariable Long commentId,
        @PathVariable LikeStatus status
    ) {
        try {
            communityCommentLikeDislikeService.changeCommunityCommentLikeDislike(communityId, commentId, status);
            communityCommentLikeDislikeService.updateLikeDislike(commentId);
        } catch (EntityNotFoundException e) {
            communityCommentLikeDislikeService.createLikeDisLike(communityId, commentId, status);
            communityCommentLikeDislikeService.updateLikeDislike(commentId);
        }
        return ResponseEntity.ok().build();
    }


}
