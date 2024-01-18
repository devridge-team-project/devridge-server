package org.devridge.api.domain.community.controller;

import javax.persistence.EntityNotFoundException;
import org.devridge.api.domain.community.service.CommunityCommentService;
import org.devridge.api.domain.community.service.CommunityCommentLikeDislikeService;
import org.devridge.api.domain.community.entity.LikeStatus;
import org.devridge.common.dto.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/community/comment/like-dislike")
@RestController
public class CommunityCommentLikeDislikeController {

    private CommunityCommentLikeDislikeService communityCommentLikeDislikeService;
    private CommunityCommentService communityCommentService;

    @Autowired
    public CommunityCommentLikeDislikeController(
        CommunityCommentLikeDislikeService communityCommentLikeDislikeService,
        CommunityCommentService communityCommentService) {
        this.communityCommentLikeDislikeService = communityCommentLikeDislikeService;
        this.communityCommentService = communityCommentService;
    }


    @PostMapping("/{commentId}/{status}")
    public ResponseEntity<?> likeDislikeCreate(@PathVariable Long commentId,
        @PathVariable LikeStatus status) {
        String message;
        if (status.equals(LikeStatus.G)) {
            message = "좋아요를 눌렀습니다.";
        } else {
            message = "싫어요를 눌렀습니다.";
        }
        try {
            communityCommentLikeDislikeService.changeCommunityCommentLikeDislike(commentId, status);
            communityCommentService.updateLikeDislike(commentId);
        } catch (EntityNotFoundException e) {
            communityCommentLikeDislikeService.createLikeDisLike(commentId, status);
            communityCommentService.updateLikeDislike(commentId);
        }
        BaseResponse response = new BaseResponse(
            HttpStatus.OK.value(),
            message
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


}
