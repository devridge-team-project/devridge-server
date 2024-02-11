package org.devridge.api.domain.qna.service;

import lombok.RequiredArgsConstructor;

import org.devridge.api.domain.member.entity.Member;
import org.devridge.api.domain.member.repository.MemberRepository;
import org.devridge.api.domain.qna.dto.request.CreateQnACommentRequest;
import org.devridge.api.domain.qna.dto.request.UpdateQnACommentRequest;
import org.devridge.api.domain.qna.dto.response.GetAllCommentByQnAId;
import org.devridge.api.domain.qna.dto.type.LikeStatus;
import org.devridge.api.domain.qna.entity.QnACommentLikeDislike;
import org.devridge.api.domain.qna.entity.QnA;
import org.devridge.api.domain.qna.entity.QnAComment;
import org.devridge.api.domain.qna.entity.id.QnACommentLikeDislikeId;
import org.devridge.api.domain.qna.mapper.QnACommentMapper;
import org.devridge.api.domain.qna.repository.QnACommentLikeDislikeRepository;
import org.devridge.api.domain.qna.repository.QnACommentRepository;
import org.devridge.api.domain.qna.repository.QnAQuerydslRepository;
import org.devridge.api.domain.qna.repository.QnARepository;
import org.devridge.common.exception.DataNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.devridge.api.util.SecurityContextHolderUtil.getMemberId;

@RequiredArgsConstructor
@Service
public class QnACommentService {

    private final QnARepository qnaRepository;
    private final QnACommentRepository qnaCommentRepository;
    private final MemberRepository memberRepository;
    private final QnACommentMapper qnaCommentMapper;
    private final QnACommentLikeDislikeRepository qnaCommentLikeDislikeRepository;
    private final QnAQuerydslRepository qnaQuerydslRepository;

    public List<GetAllCommentByQnAId> getAllQnACommentByQnAId(Long lastIndex, Long qnaId) {
        QnA qna = getQnA(qnaId);

        if (lastIndex == null) {
            // TODO: 해당 게시글에 댓글이 없는 경우 확인
            Long lastIndexByQnAId = qnaCommentRepository.findMaxIdByQnAId(qnaId);
            return qnaQuerydslRepository.findAllQnAComment(lastIndexByQnAId, qnaId);
        }

        // TODO: 탈퇴된 멤버 처리 확인
        return qnaQuerydslRepository.findAllQnAComment(lastIndex, qnaId);
    }

    public Long createQnAComment(Long qnaId, CreateQnACommentRequest commentRequest) {
        QnA qna = getQnA(qnaId);
        Member member = getMember();

        QnAComment comment = qnaCommentMapper.toQnAComment(member, qna, commentRequest);

        return qnaCommentRepository.save(comment).getId();
    }

    @Transactional
    public void updateQnAComment(Long qnaId, Long commentId, UpdateQnACommentRequest commentRequest) {
        getQnA(qnaId);
        getQnAComment(commentId);

        qnaCommentRepository.updateQnAComment(commentRequest.getContent(), commentId);
    }

    @Transactional
    public void deleteQnAComment(Long qnaId, Long commentId) {
        getQnA(qnaId);
        getQnAComment(commentId);

        qnaCommentRepository.deleteById(commentId);
    }

    @Transactional
    public void createQnACommentLike(Long qnaId, Long commentId) {
        Member member = this.getMember();
        QnA qna = this.getQnA(qnaId);
        QnAComment qnaComment = this.getQnAComment(commentId);
        QnACommentLikeDislikeId id = new QnACommentLikeDislikeId(member, qnaComment);

        qnaCommentLikeDislikeRepository.findById(id)
            .ifPresentOrElse(
                result -> {
                    switch (result.getStatus()) {
                        case G:
                            qnaCommentLikeDislikeRepository.updateDeleteStatus(member, qnaComment);
                            break;

                        case B:
                            qnaCommentLikeDislikeRepository.updateQnACommentLikeStatusToGoodOrBad(
                                member,
                                qnaComment,
                                false,
                                LikeStatus.G
                            );
                            break;
                    }
                },
                () -> {
                    QnACommentLikeDislike likeDislike = new QnACommentLikeDislike(id, LikeStatus.valueOf("G"));
                    qnaCommentLikeDislikeRepository.save(likeDislike);
                }
            );

        this.updateLikesAndDislikes(qnaComment);
    }

    @Transactional
    public void createQnACommentDislike(Long qnaId, Long commentId) {
        Member member = this.getMember();
        QnA qna = this.getQnA(qnaId);
        QnAComment qnaComment = this.getQnAComment(commentId);
        QnACommentLikeDislikeId id = new QnACommentLikeDislikeId(member, qnaComment);

        qnaCommentLikeDislikeRepository.findById(id)
            .ifPresentOrElse(
                result -> {
                    switch (result.getStatus()) {
                        case G:
                            qnaCommentLikeDislikeRepository.updateQnACommentLikeStatusToGoodOrBad(
                                member,
                                qnaComment,
                                false,
                                LikeStatus.B
                            );
                            break;

                        case B:
                            qnaCommentLikeDislikeRepository.updateDeleteStatus(member, qnaComment);
                            break;
                    }
                },
                () -> {
                    QnACommentLikeDislike likeDislike = new QnACommentLikeDislike(id, LikeStatus.valueOf("B"));
                    qnaCommentLikeDislikeRepository.save(likeDislike);
                }
            );

        this.updateLikesAndDislikes(qnaComment);
    }

    private QnA getQnA(Long qnaId) {
        return qnaRepository.findById(qnaId).orElseThrow(() -> new DataNotFoundException());
    }

    private Member getMember() {
        Long memberId = getMemberId();
        return memberRepository.findById(memberId).orElseThrow(() -> new DataNotFoundException());
    }

    private QnAComment getQnAComment(Long commentId) {
        return qnaCommentRepository.findById(commentId).orElseThrow(() -> new DataNotFoundException());
    }

    private void updateLikesAndDislikes(QnAComment qnaComment) {
        // TODO: 추후 batch 혹은 Trigger 사용
        int likes = qnaCommentLikeDislikeRepository.countQnACommentLikeOrDislikeByQnAId(
            qnaComment,
            LikeStatus.G,
            false
        );
        int dislikes = qnaCommentLikeDislikeRepository.countQnACommentLikeOrDislikeByQnAId(qnaComment, LikeStatus.B, false);
        qnaCommentRepository.updateLikeAndDiscount(likes, dislikes, qnaComment.getId());
    }
}
