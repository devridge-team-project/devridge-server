package org.devridge.api.domain.qna.service;

import lombok.RequiredArgsConstructor;

import org.devridge.api.domain.member.entity.Member;
import org.devridge.api.domain.member.repository.MemberRepository;
import org.devridge.api.domain.qna.dto.request.CreateQnACommentRequest;
import org.devridge.api.domain.qna.dto.request.UpdateQnACommentRequest;
import org.devridge.api.domain.qna.entity.QnA;
import org.devridge.api.domain.qna.entity.QnAComment;
import org.devridge.api.domain.qna.mapper.QnACommentMapper;
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

    public Long createQnAComment(Long qnaId, CreateQnACommentRequest commentRequest) {
        QnA qna = checkQnAValidate(qnaId);

        Long writerId = getMemberId();
        Member member = findMemberById(writerId);

        QnAComment comment = qnaCommentMapper.toQnAComment(member, qna, commentRequest);

        return qnaCommentRepository.save(comment).getId();
    }

    @Transactional
    public void updateQnAComment(Long qnaId, Long commentId, UpdateQnACommentRequest commentRequest) {
        checkQnAValidate(qnaId);
        checkQnACommentValidate(commentId);

        qnaCommentRepository.updateQnAComment(commentId, commentRequest.getContent());
    }

    @Transactional
    public void deleteQnAComment(Long qnaId, Long commentId) {
        checkQnAValidate(qnaId);
        checkQnACommentValidate(commentId);

        qnaCommentRepository.deleteById(commentId);
    }

    private QnA checkQnAValidate(Long qnaId) {
        return qnaRepository.findById(qnaId).orElseThrow(() -> new DataNotFoundException());
    }

    private Member findMemberById(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(() -> new DataNotFoundException());
    }

    private void checkQnACommentValidate(Long commentId) {
        qnaCommentRepository.findById(commentId).orElseThrow(() -> new DataNotFoundException());
    }
}
