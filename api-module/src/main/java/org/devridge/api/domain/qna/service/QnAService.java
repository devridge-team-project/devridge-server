package org.devridge.api.domain.qna.service;

import lombok.RequiredArgsConstructor;

import org.devridge.api.domain.qna.dto.request.CreateQnARequest;
import org.devridge.api.domain.qna.dto.request.UpdateQnARequest;
import org.devridge.api.domain.qna.dto.response.GetAllQnAResponse;
import org.devridge.api.domain.qna.dto.response.GetQnADetailResponse;
import org.devridge.api.domain.qna.entity.QnA;
import org.devridge.api.domain.qna.mapper.QnAMapper;
import org.devridge.api.domain.qna.repository.QnAQuerydslRepository;
import org.devridge.api.domain.qna.repository.QnARepository;
import org.devridge.api.domain.member.repository.MemberRepository;
import org.devridge.api.domain.member.entity.Member;
import org.devridge.common.exception.DataNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.devridge.api.util.SecurityContextHolderUtil.getMemberId;

@RequiredArgsConstructor
@Service
public class QnAService {

    private final QnARepository qnaRepository;
    private final QnAMapper qnaMapper;
    private final QnAQuerydslRepository qnaQuerydslRepository;
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public List<GetAllQnAResponse> getAllQnASortByViews(String sortOption) {
        // TODO: 추후 24시 기준으로 업데이트
        if (sortOption.equals("views")) {
            return qnaQuerydslRepository.findAllQnASortByViews();
        }

        // TODO: 무한 스크롤 변경 예정
        return qnaQuerydslRepository.findAllQnASortByLatest();
    }

    @Transactional(readOnly = true)
    public GetQnADetailResponse getQnADetail(Long qnaId) {
        QnA result = this.checkQnAValidate(qnaId);
        return qnaMapper.toGetQnADetailResponse(result);
    }

    public Long createQnA(CreateQnARequest qnaRequest) {
        Long writerId = getMemberId();
        Member member = memberRepository.findById(writerId).orElseThrow(() -> new DataNotFoundException());

        QnA qna = qnaMapper.toQnA(qnaRequest, member);

        return qnaRepository.save(qna).getId();
    }

    @Transactional
    public void updateQnA(Long qnaId, UpdateQnARequest qnaRequest) {
        checkQnAValidate(qnaId);
        qnaRepository.updateQnA(qnaRequest.getTitle(), qnaRequest.getContent(), qnaId);
    }

    public void deleteQnA(Long qnaId) {
        checkQnAValidate(qnaId);
        qnaRepository.deleteById(qnaId);
    }

    private QnA checkQnAValidate(Long qnaId) {
        return qnaRepository.findById(qnaId).orElseThrow(() -> new DataNotFoundException());
    }
}
