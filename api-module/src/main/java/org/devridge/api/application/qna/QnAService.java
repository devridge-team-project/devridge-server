package org.devridge.api.application.qna;

import lombok.RequiredArgsConstructor;

import org.devridge.api.domain.qna.dto.request.CreateQnARequest;
import org.devridge.api.domain.qna.dto.request.UpdateQnARequest;
import org.devridge.api.domain.qna.dto.response.GetAllQnAResponse;
import org.devridge.api.domain.qna.dto.response.GetQnADetailResponse;
import org.devridge.api.domain.qna.dto.type.LikeStatus;
import org.devridge.api.domain.qna.entity.QnA;
import org.devridge.api.domain.qna.entity.QnALikeDislike;
import org.devridge.api.domain.qna.entity.id.QnALikeDislikeId;
import org.devridge.api.infrastructure.qna.QnALikeDislikeRepository;
import org.devridge.api.infrastructure.qna.QnAQuerydslRepository;
import org.devridge.api.infrastructure.qna.QnARepository;
import org.devridge.api.infrastructure.member.MemberRepository;
import org.devridge.api.domain.member.entity.Member;
import org.devridge.api.infrastructure.qna.QnAScrapRepository;
import org.devridge.api.application.s3.S3Service;
import org.devridge.api.common.exception.common.DataNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.devridge.api.common.util.SecurityContextHolderUtil.getMemberId;

@RequiredArgsConstructor
@Service
public class QnAService {

    private final QnARepository qnaRepository;
    private final QnAMapper qnaMapper;
    private final QnAQuerydslRepository qnaQuerydslRepository;
    private final MemberRepository memberRepository;
    private final QnALikeDislikeRepository qnaLikeDislikeRepository;
    private final QnAScrapRepository qnaScrapRepository;
    private final S3Service s3Service;

    @Transactional(readOnly = true)
    public List<GetAllQnAResponse> getAllQnA(String sortOption, Long lastIndex) {
        // TODO: 추후 24시 기준으로 업데이트
        if (sortOption.equals("views")) {
            return qnaQuerydslRepository.findAllQnASortByViews();
        }

        if (lastIndex == null) {
            Long maxId = qnaRepository.findMaxId().orElse(0L);
            List<QnA> qna = qnaQuerydslRepository.findAllQnASortByLatest(maxId);
            return qnaMapper.toGetAllQnAResponses(qna);
        }

        List<QnA> qna = qnaQuerydslRepository.findAllQnASortByLatest(lastIndex);
        return qnaMapper.toGetAllQnAResponses(qna);
    }

    @Transactional
    public GetQnADetailResponse getQnADetail(Long qnaId) {
        QnA qna = this.getQnA(qnaId);
        qnaRepository.increaseQnAView(qnaId);

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
        QnA qna = getQnA(qnaId);
        List<String> images = new ArrayList<>(Arrays.asList(qna.getImageUrl().split(", ")));

        qnaRepository.deleteById(qnaId);
        s3Service.deleteAllImage(images);
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
                            qnaLikeDislikeRepository.updateQnALikeStatusToGoodOrBad(
                                member,
                                qna,
                                false,
                                LikeStatus.G
                            );
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
                            qnaLikeDislikeRepository.updateQnALikeStatusToGoodOrBad(
                                member,
                                qna,
                                false,
                                LikeStatus.B
                            );
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
        int likes = qnaLikeDislikeRepository.countQnALikeOrDislikeByQnAId(qna, LikeStatus.G, false);
        int dislikes = qnaLikeDislikeRepository.countQnALikeOrDislikeByQnAId(qna, LikeStatus.B, false);
        qnaRepository.updateLikeAndDiscount(likes, dislikes, qna.getId());
    }
}
