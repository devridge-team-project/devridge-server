package org.devridge.api.domain.qna.service;

import lombok.RequiredArgsConstructor;

import org.devridge.api.domain.qna.dto.response.GetAllQnAResponse;
import org.devridge.api.domain.qna.entity.QnA;
import org.devridge.api.domain.qna.mapper.QnAMapper;
import org.devridge.api.domain.qna.repository.QnARepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class QnAService {

    private final QnARepository qnaRepository;
    private final QnAMapper qnaMapper;

    @Transactional(readOnly = true)
    public List<GetAllQnAResponse> getAllQnA() {
        // TODO: 추후 24시 기준으로 업데이트
        List<QnA> findResult = qnaRepository.findAll();
        return qnaMapper.toGetAllQnAResponse(findResult);
    }
}
