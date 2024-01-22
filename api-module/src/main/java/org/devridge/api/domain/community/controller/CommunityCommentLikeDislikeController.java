package org.devridge.api.domain.community.controller;

import javax.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.devridge.api.domain.community.entity.LikeStatus;
import org.devridge.api.domain.community.service.CommunityCommentLikeDislikeService;
import org.devridge.api.domain.community.service.CommunityCommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/community/comment/{commentId}/like-dislike/{status}")
@RestController
public class CommunityCommentLikeDislikeController {

    private final CommunityCommentLikeDislikeService communityCommentLikeDislikeService;
    private final CommunityCommentService communityCommentService;

    @PostMapping
    public ResponseEntity<?> likeDislikeCreate(
        @PathVariable Long commentId,
        @PathVariable LikeStatus status
    ) {
        try {
            communityCommentLikeDislikeService.changeCommunityCommentLikeDislike(commentId, status);
            communityCommentService.updateLikeDislike(commentId);
        } catch (EntityNotFoundException e) {
            communityCommentLikeDislikeService.createLikeDisLike(commentId, status);
            communityCommentService.updateLikeDislike(commentId);
        }
        return ResponseEntity.ok().build();
    }


}
