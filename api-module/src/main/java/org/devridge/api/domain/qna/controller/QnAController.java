package org.devridge.api.domain.qna.controller;

import lombok.RequiredArgsConstructor;

import org.devridge.api.domain.qna.dto.response.GetAllQnAResponse;
import org.devridge.api.domain.qna.dto.response.GetQnADetailResponse;
import org.devridge.api.domain.qna.service.QnAService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/api/qna")
@RequiredArgsConstructor
@RestController
public class QnAController {

    private final QnAService qnaService;

    @GetMapping("/views")
    public ResponseEntity<List<GetAllQnAResponse>> getAllQnASortByViews() {
        List<GetAllQnAResponse> result = qnaService.getAllQnASortByViews();
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/latest")
    public ResponseEntity<List<GetAllQnAResponse>> getAllQnASortByLatest() {
        List<GetAllQnAResponse> result = qnaService.getAllQnASortByLatest();
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/{qnaId}")
    public ResponseEntity<GetQnADetailResponse> getQnADetail(@PathVariable Long qnaId) {
        GetQnADetailResponse result = qnaService.getQnADetail(qnaId);
        return ResponseEntity.ok().body(result);
    }
}
