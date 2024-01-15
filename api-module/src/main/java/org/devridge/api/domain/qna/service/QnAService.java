package org.devridge.api.domain.qna.service;

import lombok.RequiredArgsConstructor;

import org.devridge.api.domain.qna.dto.response.GetAllQnAResponse;
import org.devridge.api.domain.qna.dto.response.GetQnADetailResponse;
import org.devridge.api.domain.qna.entity.QnA;
import org.devridge.api.domain.qna.mapper.QnAMapper;
import org.devridge.api.domain.qna.repository.QnAQuerydslRepository;
import org.devridge.api.domain.qna.repository.QnARepository;

import org.devridge.common.exception.DataNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class QnAService {

    private final QnARepository qnaRepository;
    private final QnAMapper qnaMapper;
    private final QnAQuerydslRepository qnaQuerydslRepository;

    @Transactional(readOnly = true)
    public List<GetAllQnAResponse> getAllQnASortByViews() {
        // TODO: 추후 24시 기준으로 업데이트
        return qnaQuerydslRepository.findAllQnASortByViews();
    }

    @Transactional(readOnly = true)
    public List<GetAllQnAResponse> getAllQnASortByLatest() {
        // TODO: 무한 스크롤 변경 예정
        return qnaQuerydslRepository.findAllQnASortByLatest();
    }

    public GetQnADetailResponse getQnADetail(Long qnaId) {
        QnA result = qnaRepository.findById(qnaId).orElseThrow(() -> new DataNotFoundException());
        return qnaMapper.toGetQnADetailResponse(result);
    }
}
