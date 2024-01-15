package org.devridge.api.domain.communitycommentlikedislike;

import javax.persistence.EntityNotFoundException;
import org.devridge.common.dto.BaseResponse;
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
        @PathVariable LikeStatus status) {
        String message;
        if (status.equals(LikeStatus.G)) {
            message = "좋아요를 눌렀습니다.";
        } else {
            message = "싫어요를 눌렀습니다.";
        }
        try {
            communityCommentLikeDislikeService.changeCommunityCommentLikeDislike(memberId, commentId, status);
        } catch (EntityNotFoundException e) {
            communityCommentLikeDislikeService.createLikeDisLike(memberId, commentId, status);
        }
        BaseResponse response = new BaseResponse(
            HttpStatus.OK.value(),
            message
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


}
