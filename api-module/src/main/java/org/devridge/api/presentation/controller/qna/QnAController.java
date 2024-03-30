package org.devridge.api.presentation.controller.qna;

import lombok.RequiredArgsConstructor;

import org.devridge.api.domain.qna.dto.request.*;
import org.devridge.api.domain.qna.dto.response.GetAllQnAResponse;
import org.devridge.api.domain.qna.dto.response.GetQnADetailResponse;
import org.devridge.api.domain.qna.dto.type.SortOption;
import org.devridge.api.application.qna.QnAService;
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

    /**
     * Q&A 리스트 조회(조회수, 최신순) - 조회수의 경우 상위 5개만 반환, 최신순의 경우 무한 스크롤
     * @param sortOption
     * @param lastIndex
     * @return Q&A 리스트
     */
    @GetMapping
    public ResponseEntity<List<GetAllQnAResponse>> getAllQnA(
        @RequestParam(value = "sortOption", required = true)
        @ValidateSortOption(enumClass = SortOption.class) SortOption sortOption,
        @RequestParam(value = "lastIndex", required = false) Long lastIndex
    ) {
        List<GetAllQnAResponse> result = qnaService.getAllQnA(sortOption.toString(), lastIndex);
        return ResponseEntity.ok().body(result);
    }

    /**
     * Q&A 세부 내용 조회
     * @param qnaId
     * @return Q&A 세부 내용
     */
    @GetMapping("/{qnaId}")
    public ResponseEntity<GetQnADetailResponse> getQnADetail(@PathVariable Long qnaId) {
        GetQnADetailResponse result = qnaService.getQnADetail(qnaId);
        return ResponseEntity.ok().body(result);
    }

    /**
     * Q&A 생성
     * @param qnaRequest
     */
    @PostMapping
    public ResponseEntity<Void> createQnA(@Valid @RequestBody CreateQnARequest qnaRequest) {
        Long qnaId = qnaService.createQnA(qnaRequest);
        return ResponseEntity.created(URI.create("/api/qna/" + qnaId)).build();
    }

    /**
     * Q&A 수정
     * @param qnaId
     * @param qnaRequest
     */
    @PatchMapping("/{qnaId}")
    public ResponseEntity<Void> updateQnA(
        @PathVariable Long qnaId,
        @Valid @RequestBody UpdateQnARequest qnaRequest
    ) {
        qnaService.updateQnA(qnaId, qnaRequest);
        return ResponseEntity.ok().build();
    }

    /**
     * Q&A 삭제
     * @param qnaId
     */
    @DeleteMapping("/{qnaId}")
    public ResponseEntity<Void> deleteQnA(@PathVariable Long qnaId) {
        qnaService.deleteQnA(qnaId);
        return ResponseEntity.ok().build();
    }

    /**
     * Q&A 추천
     * @param qnaId
     */
    @PostMapping("/like/{qnaId}")
    public ResponseEntity<Void> createQnALike(@PathVariable Long qnaId) {
        qnaService.createQnALike(qnaId);
        return ResponseEntity.ok().build();
    }

    /**
     * Q&A 비추천
     * @param qnaId
     */
    @PostMapping("/dislike/{qnaId}")
    public ResponseEntity<Void> createQnADislike(@PathVariable Long qnaId) {
        qnaService.createQnADislike(qnaId);
        return ResponseEntity.ok().build();
    }

    /**
     * Q&A 스크랩
     * @param qnaId
     */
    @PostMapping("/bookmarks/{qnaId}")
    public ResponseEntity<Void> createQnAScrap(@PathVariable Long qnaId) {
        qnaService.createQnAScrap(qnaId);
        return ResponseEntity.ok().build();
    }
}
