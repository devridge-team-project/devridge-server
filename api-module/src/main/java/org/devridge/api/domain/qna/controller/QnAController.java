package org.devridge.api.domain.qna.controller;

import lombok.RequiredArgsConstructor;

import org.devridge.api.domain.qna.dto.request.*;
import org.devridge.api.domain.qna.dto.response.GetAllQnAResponse;
import org.devridge.api.domain.qna.dto.response.GetQnADetailResponse;
import org.devridge.api.domain.qna.dto.type.SortOption;
import org.devridge.api.domain.qna.service.QnACommentService;
import org.devridge.api.domain.qna.service.QnAService;
import org.devridge.api.domain.qna.validator.ValidateSortOption;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.net.URI;
import java.util.List;

@RequestMapping("/api/qna")
@RequiredArgsConstructor
@RestController
public class QnAController {

    private final QnAService qnaService;
    private final QnACommentService qnaCommentService;

    @GetMapping
    public ResponseEntity<List<GetAllQnAResponse>> getAllQnASortByViews(
        @RequestParam(value = "sortOption", required = true)
        @ValidateSortOption(enumClass = SortOption.class) SortOption sortOption
    ) {
        List<GetAllQnAResponse> result = qnaService.getAllQnASortByViews(sortOption.toString());
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/{qnaId}")
    public ResponseEntity<GetQnADetailResponse> getQnADetail(@PathVariable Long qnaId) {
        GetQnADetailResponse result = qnaService.getQnADetail(qnaId);
        return ResponseEntity.ok().body(result);
    }

    @PostMapping
    public ResponseEntity<Void> createQnA(@Valid @RequestBody CreateQnARequest qnaRequest) {
        Long qnaId = qnaService.createQnA(qnaRequest);
        return ResponseEntity.created(URI.create("/api/qna/" + qnaId)).build();
    }

    @PutMapping("/{qnaId}")
    public ResponseEntity<Void> updateQnA(
        @PathVariable Long qnaId,
        @Valid @RequestBody UpdateQnARequest qnaRequest
    ) {
        qnaService.updateQnA(qnaId, qnaRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{qnaId}")
    public ResponseEntity<Void> deleteQnA(@PathVariable Long qnaId) {
        qnaService.deleteQnA(qnaId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{qnaId}/comments")
    public ResponseEntity<Void> createQnAComment(
        @PathVariable Long qnaId,
        @RequestBody CreateQnACommentRequest commentRequest
    ) {
        Long commentId = qnaCommentService.createQnAComment(qnaId, commentRequest);
        return ResponseEntity.created(URI.create("/api/qna/" + qnaId + "/comments/" + commentId)).build();
    }

    @PutMapping("/{qnaId}/comments/{commentId}")
    public ResponseEntity<Void> updateQnAComment(
        @PathVariable Long qnaId,
        @PathVariable Long commentId,
        @RequestBody UpdateQnACommentRequest commentRequest
    ) {
        qnaCommentService.updateQnAComment(qnaId, commentId, commentRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{qnaId}/comments/{commentId}")
    public ResponseEntity<Void> deleteQnAComment(
        @PathVariable Long qnaId,
        @PathVariable Long commentId
    ) {
        qnaCommentService.deleteQnAComment(qnaId, commentId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/like/{qnaId}")
    public ResponseEntity<Void> createLikeOrDislike(@PathVariable Long qnaId) {
        qnaService.createLikeOrDislike(qnaId);
        return ResponseEntity.ok().build();
    }
}
