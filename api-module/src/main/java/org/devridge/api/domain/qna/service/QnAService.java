package org.devridge.api.domain.qna.service;

import lombok.RequiredArgsConstructor;

import org.devridge.api.domain.qna.dto.request.CreateQnARequest;
import org.devridge.api.domain.qna.dto.request.UpdateQnARequest;
import org.devridge.api.domain.qna.dto.response.GetAllQnAResponse;
import org.devridge.api.domain.qna.dto.response.GetQnADetailResponse;
import org.devridge.api.domain.qna.dto.type.LikeStatus;
import org.devridge.api.domain.qna.entity.QnA;
import org.devridge.api.domain.qna.entity.QnALikeDislike;
import org.devridge.api.domain.qna.entity.id.QnALikeDislikeId;
import org.devridge.api.domain.qna.mapper.QnAMapper;
import org.devridge.api.domain.qna.repository.QnALikeDislikeRepository;
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
    private final QnALikeDislikeRepository qnaLikeDislikeRepository;

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
        QnA qna = this.checkQnAValidate(qnaId);
        return qnaMapper.toGetQnADetailResponse(qna);
    }

    public Long createQnA(CreateQnARequest qnaRequest) {
        Member member = this.getMember();
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

    @Transactional
    public void createLike(Long qnaId) {
        Member member = this.getMember();
        QnA qna = this.checkQnAValidate(qnaId);
        QnALikeDislikeId id = new QnALikeDislikeId(member, qna);

        qnaLikeDislikeRepository.findById(id)
            .ifPresentOrElse(
                result -> {
                    switch (result.getStatus()) {
                        case G:
                            qnaLikeDislikeRepository.deleteById(member, qna);
                            break;

                        case B:
                            qnaLikeDislikeRepository.updateQnALikeStatusToGood(member, qna);
                            break;
                    }
                },
                () -> {
                    QnALikeDislike likeDislike = new QnALikeDislike(id, LikeStatus.valueOf("G"));
                    qnaLikeDislikeRepository.save(likeDislike);
                }
            );
    }

    @Transactional
    public void createDislike(Long qnaId) {
        Member member = this.getMember();
        QnA qna = this.checkQnAValidate(qnaId);
        QnALikeDislikeId id = new QnALikeDislikeId(member, qna);

        qnaLikeDislikeRepository.findById(id)
            .ifPresentOrElse(
                result -> {
                    switch (result.getStatus()) {
                        case G:
                            qnaLikeDislikeRepository.updateQnALikeStatusToBad(member, qna);
                            break;

                        case B:
                            qnaLikeDislikeRepository.deleteById(member, qna);
                            break;
                    }
                },
                () -> {
                    QnALikeDislike likeDislike = new QnALikeDislike(id, LikeStatus.valueOf("B"));
                    qnaLikeDislikeRepository.save(likeDislike);
                }
            );
    }

    private QnA checkQnAValidate(Long qnaId) {
        return qnaRepository.findById(qnaId).orElseThrow(() -> new DataNotFoundException());
    }

    private Member getMember() {
        Long memberId = getMemberId();
        return memberRepository.findById(memberId).orElseThrow(() -> new DataNotFoundException());
    }
}
