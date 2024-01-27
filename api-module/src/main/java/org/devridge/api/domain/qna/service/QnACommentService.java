package org.devridge.api.domain.qna.service;

import lombok.RequiredArgsConstructor;

import org.devridge.api.domain.member.entity.Member;
import org.devridge.api.domain.member.repository.MemberRepository;
import org.devridge.api.domain.qna.dto.request.CreateQnACommentRequest;
import org.devridge.api.domain.qna.dto.request.UpdateQnACommentRequest;
import org.devridge.api.domain.qna.dto.type.LikeStatus;
import org.devridge.api.domain.qna.entity.QnACommentLikeDislike;
import org.devridge.api.domain.qna.entity.QnA;
import org.devridge.api.domain.qna.entity.QnAComment;
import org.devridge.api.domain.qna.entity.id.QnACommentLikeDislikeId;
import org.devridge.api.domain.qna.mapper.QnACommentMapper;
import org.devridge.api.domain.qna.repository.CommentLikeDislikeRepository;
import org.devridge.api.domain.qna.repository.QnACommentRepository;
import org.devridge.api.domain.qna.repository.QnARepository;
import org.devridge.common.exception.DataNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.devridge.api.util.SecurityContextHolderUtil.getMemberId;

@RequiredArgsConstructor
@Service
public class QnACommentService {

    private final QnARepository qnaRepository;
    private final QnACommentRepository qnaCommentRepository;
    private final MemberRepository memberRepository;
    private final QnACommentMapper qnaCommentMapper;
    private final CommentLikeDislikeRepository commentLikeDislikeRepository;

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

        commentLikeDislikeRepository.findById(id)
            .ifPresentOrElse(
                result -> {
                    switch (result.getStatus()) {
                        case G:
                            this.updateDeleteStatus(result, id);
                            break;

                        case B:
                            if (result.getIsDeleted()) {
                                commentLikeDislikeRepository.recoverLikeDislike(member, qnaComment);
                            }
                            commentLikeDislikeRepository.updateQnACommentLikeStatusToGood(member, qnaComment);
                            break;
                    }
                },
                () -> {
                    QnACommentLikeDislike likeDislike = new QnACommentLikeDislike(id, LikeStatus.valueOf("G"));
                    commentLikeDislikeRepository.save(likeDislike);
                }
            );
    }

    @Transactional
    public void createQnACommentDislike(Long qnaId, Long commentId) {
        Member member = this.getMember();
        QnA qna = this.getQnA(qnaId);
        QnAComment qnaComment = this.getQnAComment(commentId);
        QnACommentLikeDislikeId id = new QnACommentLikeDislikeId(member, qnaComment);

        commentLikeDislikeRepository.findById(id)
            .ifPresentOrElse(
                result -> {
                    switch (result.getStatus()) {
                        case G:
                            if (result.getIsDeleted()) {
                                commentLikeDislikeRepository.recoverLikeDislike(member, qnaComment);
                            }
                            commentLikeDislikeRepository.updateQnACommentLikeStatusToBad(member, qnaComment);
                            break;

                        case B:
                            this.updateDeleteStatus(result, id);
                            break;
                    }
                },
                () -> {
                    QnACommentLikeDislike likeDislike = new QnACommentLikeDislike(id, LikeStatus.valueOf("B"));
                    commentLikeDislikeRepository.save(likeDislike);
                }
            );
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

    private void updateDeleteStatus(QnACommentLikeDislike likeDislike, QnACommentLikeDislikeId id) {
        if (!likeDislike.getIsDeleted()) {
            commentLikeDislikeRepository.deleteById(id.getMember(), id.getQnaComment());
        } else {
            commentLikeDislikeRepository.recoverLikeDislike(id.getMember(), id.getQnaComment());
        }
    }
}
