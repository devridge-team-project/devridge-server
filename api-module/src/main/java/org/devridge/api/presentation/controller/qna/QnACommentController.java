package org.devridge.api.presentation.controller.qna;

import lombok.RequiredArgsConstructor;

import org.devridge.api.domain.qna.dto.request.CreateQnACommentRequest;
import org.devridge.api.domain.qna.dto.request.UpdateQnACommentRequest;
import org.devridge.api.domain.qna.dto.response.GetAllCommentByQnAId;
import org.devridge.api.application.qna.QnACommentService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RequestMapping("/api/qna/{qnaId}/comments")
@RequiredArgsConstructor
@RestController
public class QnACommentController {

    private final QnACommentService qnaCommentService;

    /**
     * 해당 Q&A 댓글 리스트 조회
     * @param qnaId
     * @param lastIndex
     * @return Q&A 댓글 무한 스크롤
     */
    @GetMapping
    public ResponseEntity<List<GetAllCommentByQnAId>> getAllQnAComment(
        @PathVariable Long qnaId,
        @RequestParam(value = "lastIndex", required = false) Long lastIndex
    ) {
        List<GetAllCommentByQnAId> comments = qnaCommentService.getAllQnACommentByQnAId(lastIndex, qnaId);
        return ResponseEntity.ok().body(comments);
    }

    /**
     * Q&A 답글 생성
     * @param qnaId
     * @param commentRequest
     */
    @PostMapping
    public ResponseEntity<Void> createQnAComment(
        @PathVariable Long qnaId,
        @RequestBody CreateQnACommentRequest commentRequest
    ) {
        Long commentId = qnaCommentService.createQnAComment(qnaId, commentRequest);
        return ResponseEntity.created(URI.create("/api/qna/" + qnaId + "/comments/" + commentId)).build();
    }

    /**
     * Q&A 답글 수정
     * @param qnaId
     * @param commentId
     * @param commentRequest
     */
    @PatchMapping("/{commentId}")
    public ResponseEntity<Void> updateQnAComment(
        @PathVariable Long qnaId,
        @PathVariable Long commentId,
        @RequestBody UpdateQnACommentRequest commentRequest
    ) {
        qnaCommentService.updateQnAComment(qnaId, commentId, commentRequest);
        return ResponseEntity.ok().build();
    }

    /**
     * Q&A 답글 삭제
     * @param qnaId
     * @param commentId
     */
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteQnAComment(
        @PathVariable Long qnaId,
        @PathVariable Long commentId
    ) {
        qnaCommentService.deleteQnAComment(qnaId, commentId);
        return ResponseEntity.ok().build();
    }

    /**
     * Q&A 답글 추천
     * @param qnaId
     * @param commentId
     */
    @PostMapping("/like/{commentId}")
    public ResponseEntity<Void> createQnACommentLike(
        @PathVariable Long qnaId,
        @PathVariable Long commentId
    ) {
        qnaCommentService.createQnACommentLike(qnaId, commentId);
        return ResponseEntity.ok().build();
    }

    /**
     * Q&A 답글 비추천
     * @param qnaId
     * @param commentId
     */
    @PostMapping("/dislike/{commentId}")
    public ResponseEntity<Void> createQnACommentDislike(
        @PathVariable Long qnaId,
        @PathVariable Long commentId
    ) {
        qnaCommentService.createQnACommentDislike(qnaId, commentId);
        return ResponseEntity.ok().build();
    }
}
