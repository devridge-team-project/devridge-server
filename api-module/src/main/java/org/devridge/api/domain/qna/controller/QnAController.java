package org.devridge.api.domain.qna.controller;

import lombok.RequiredArgsConstructor;

import org.devridge.api.domain.qna.dto.request.*;
import org.devridge.api.domain.qna.dto.response.GetAllQnAResponse;
import org.devridge.api.domain.qna.dto.response.GetQnADetailResponse;
import org.devridge.api.domain.qna.dto.type.SortOption;
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

    @GetMapping
    public ResponseEntity<List<GetAllQnAResponse>> getAllQnA(
        @RequestParam(value = "sortOption", required = true)
        @ValidateSortOption(enumClass = SortOption.class) SortOption sortOption,
        @RequestParam(value = "lastIndex", required = false) Long lastIndex
    ) {
        List<GetAllQnAResponse> result = qnaService.getAllQnASort(sortOption.toString(), lastIndex);
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

    @PatchMapping("/{qnaId}")
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

    @PostMapping("/like/{qnaId}")
    public ResponseEntity<Void> createQnALike(@PathVariable Long qnaId) {
        qnaService.createQnALike(qnaId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/dislike/{qnaId}")
    public ResponseEntity<Void> createQnADislike(@PathVariable Long qnaId) {
        qnaService.createQnADislike(qnaId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/bookmarks/{qnaId}")
    public ResponseEntity<Void> createQnAScrap(@PathVariable Long qnaId) {
        qnaService.createQnAScrap(qnaId);
        return ResponseEntity.ok().build();
    }
}
