package org.devridge.api.domain.community.controller;

import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.devridge.api.domain.community.dto.request.CommunityCommentRequest;
import org.devridge.api.domain.community.dto.response.CommunityCommentResponse;
import org.devridge.api.domain.community.service.CommunityCommentService;
import org.devridge.common.dto.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/community/{communityId}/comment")
@RestController
public class CommunityCommentController {

    private final CommunityCommentService communityCommentService;

    @PostMapping//todo: memberId 검증방법
    public ResponseEntity<?> writeComment(
        @Valid @RequestBody CommunityCommentRequest commentRequest,
        @PathVariable Long communityId
    ) {
        communityCommentService.createComment(communityId, commentRequest);
        BaseResponse response = new BaseResponse(
            HttpStatus.OK.value(),
            "댓글 작성 성공"
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<CommunityCommentResponse>> viewComment(@PathVariable Long communityId) {
        List<CommunityCommentResponse> CommentResponses = communityCommentService.getAllComment(communityId);
        return ResponseEntity.ok().body(CommentResponses);
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<?> updateComment(
        @RequestBody CommunityCommentRequest commentRequest,
        @PathVariable Long commentId,
        @PathVariable Long communityId
    ) {
        communityCommentService.updateComment(commentId, commentRequest);
        BaseResponse response = new BaseResponse(
            HttpStatus.OK.value(),
            "댓글 수정 성공"
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> CommentDelete(
        @PathVariable Long communityId,
        @PathVariable Long commentId
    ) {
        communityCommentService.deleteComment(commentId);
        BaseResponse response = new BaseResponse(
            HttpStatus.OK.value(),
            "댓글 삭제 성공"
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
