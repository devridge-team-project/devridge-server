package org.devridge.api.domain.qna.controller;

import lombok.RequiredArgsConstructor;

import org.devridge.api.domain.qna.dto.request.CreateQnACommentRequest;
import org.devridge.api.domain.qna.dto.request.UpdateQnACommentRequest;
import org.devridge.api.domain.qna.service.QnACommentService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RequestMapping("/api/qna/{qnaId}/comments")
@RequiredArgsConstructor
@RestController
public class QnACommentController {

    private final QnACommentService qnaCommentService;

    @PostMapping
    public ResponseEntity<Void> createQnAComment(
        @PathVariable Long qnaId,
        @RequestBody CreateQnACommentRequest commentRequest
    ) {
        Long commentId = qnaCommentService.createQnAComment(qnaId, commentRequest);
        return ResponseEntity.created(URI.create("/api/qna/" + qnaId + "/comments/" + commentId)).build();
    }

    @PatchMapping("/{commentId}")
    public ResponseEntity<Void> updateQnAComment(
        @PathVariable Long qnaId,
        @PathVariable Long commentId,
        @RequestBody UpdateQnACommentRequest commentRequest
    ) {
        qnaCommentService.updateQnAComment(qnaId, commentId, commentRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteQnAComment(
        @PathVariable Long qnaId,
        @PathVariable Long commentId
    ) {
        qnaCommentService.deleteQnAComment(qnaId, commentId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/like/{commentId}")
    public ResponseEntity<Void> createQnACommentLike(
        @PathVariable Long qnaId,
        @PathVariable Long commentId
    ) {
        qnaCommentService.createQnACommentLike(qnaId, commentId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/dislike/{commentId}")
    public ResponseEntity<Void> createQnACommentDislike(
        @PathVariable Long qnaId,
        @PathVariable Long commentId
    ) {
        qnaCommentService.createQnACommentDislike(qnaId, commentId);
        return ResponseEntity.ok().build();
    }
}
