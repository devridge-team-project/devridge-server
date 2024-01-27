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
import org.devridge.api.domain.qna.repository.QnAScrapRepository;
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
    private final QnAScrapRepository qnaScrapRepository;

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
        QnA qna = this.getQnA(qnaId);
        return qnaMapper.toGetQnADetailResponse(qna);
    }

    public Long createQnA(CreateQnARequest qnaRequest) {
        Member member = this.getMember();
        QnA qna = qnaMapper.toQnA(qnaRequest, member);

        return qnaRepository.save(qna).getId();
    }

    @Transactional
    public void updateQnA(Long qnaId, UpdateQnARequest qnaRequest) {
        getQnA(qnaId);
        qnaRepository.updateQnA(qnaRequest.getTitle(), qnaRequest.getContent(), qnaId);
    }

    public void deleteQnA(Long qnaId) {
        getQnA(qnaId);
        qnaRepository.deleteById(qnaId);
    }

    @Transactional
    public void createQnALike(Long qnaId) {
        Member member = this.getMember();
        QnA qna = this.getQnA(qnaId);
        QnALikeDislikeId id = new QnALikeDislikeId(member, qna);

        qnaLikeDislikeRepository.findById(id)
            .ifPresentOrElse(
                result -> {
                    switch (result.getStatus()) {
                        case G:
                            qnaLikeDislikeRepository.updateDeleteStatus(member, qna);
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

        this.updateLikesAndDislikes(qna);
    }

    @Transactional
    public void createQnADislike(Long qnaId) {
        Member member = this.getMember();
        QnA qna = this.getQnA(qnaId);
        QnALikeDislikeId id = new QnALikeDislikeId(member, qna);

        qnaLikeDislikeRepository.findById(id)
            .ifPresentOrElse(
                result -> {
                    switch (result.getStatus()) {
                        case G:
                            qnaLikeDislikeRepository.updateQnALikeStatusToBad(member, qna);
                            break;

                        case B:
                            qnaLikeDislikeRepository.updateDeleteStatus(member, qna);
                            break;
                    }
                },
                () -> {
                    QnALikeDislike likeDislike = new QnALikeDislike(id, LikeStatus.valueOf("B"));
                    qnaLikeDislikeRepository.save(likeDislike);
                }
            );

        this.updateLikesAndDislikes(qna);
    }

    @Transactional
    public void createQnAScrap(Long qnaId) {
        Long memberId = getMemberId();
        qnaScrapRepository.createOrUpdateQnAScrap(memberId, qnaId);
    }

    private QnA getQnA(Long qnaId) {
        return qnaRepository.findById(qnaId).orElseThrow(() -> new DataNotFoundException());
    }

    private Member getMember() {
        Long memberId = getMemberId();
        return memberRepository.findById(memberId).orElseThrow(() -> new DataNotFoundException());
    }

    private void updateLikesAndDislikes(QnA qna) {
        // TODO: 추후 batch 혹은 Trigger 사용
        int likes = qnaLikeDislikeRepository.countQnALikeByQnAId(qna);
        int dislikes = qnaLikeDislikeRepository.countQnADislikeByQnAId(qna);
        qnaRepository.updateLikeAndDiscount(likes, dislikes, qna.getId());
    }
}
