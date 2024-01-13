package org.devridge.api.domain.communitycommentlikedislike;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CommunityCommentLikeDislikeController {

    private CommunityCommentLikeDislikeService communityCommentLikeDislikeService;

    @Autowired
    public CommunityCommentLikeDislikeController(
        CommunityCommentLikeDislikeService communityCommentLikeDislikeService) {
        this.communityCommentLikeDislikeService = communityCommentLikeDislikeService;
    }

    @PostMapping("/like/{memberId}/{commentId}/{status}")
    public ResponseEntity<?> likeDislikeCreate(@PathVariable Long memberId, @PathVariable Long commentId,
        @PathVariable Character status) {
        status = Character.toUpperCase(status);
        communityCommentLikeDislikeService.createLikeDisLike(memberId, commentId, status);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    
}
