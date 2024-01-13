package org.devridge.api.domain.qna.controller;

import lombok.RequiredArgsConstructor;
import org.devridge.api.domain.qna.dto.response.GetAllQnAResponse;
import org.devridge.api.domain.qna.entity.QnA;
import org.devridge.api.domain.qna.service.QnAService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/api/qna")
@RequiredArgsConstructor
@RestController
public class QnAController {

    private final QnAService qnaService;

    @GetMapping
    public ResponseEntity<List<GetAllQnAResponse>> getAllQnASortByToday() {
        List<GetAllQnAResponse> result = qnaService.getAllQnA();
        return ResponseEntity.ok().body(result);
    }
}
