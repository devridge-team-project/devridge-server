package org.devridge.api.domain.qna.service;

import lombok.RequiredArgsConstructor;
import org.devridge.api.domain.member.entity.Member;
import org.devridge.api.domain.member.repository.MemberRepository;
import org.devridge.api.domain.qna.dto.request.CreateQnACommentRequest;
import org.devridge.api.domain.qna.entity.QnA;
import org.devridge.api.domain.qna.entity.QnAComment;
import org.devridge.api.domain.qna.mapper.QnACommentMapper;
import org.devridge.api.domain.qna.mapper.QnAMapper;
import org.devridge.api.domain.qna.repository.QnACommentRepository;
import org.devridge.api.domain.qna.repository.QnAQuerydslRepository;
import org.devridge.api.domain.qna.repository.QnARepository;
import org.devridge.common.exception.DataNotFoundException;
import org.springframework.stereotype.Service;

import static org.devridge.api.util.SecurityContextHolderUtil.getMemberId;

@RequiredArgsConstructor
@Service
public class QnACommentService {

    private final QnARepository qnaRepository;
    private final QnACommentRepository qnACommentRepository;
    private final MemberRepository memberRepository;
    private final QnACommentMapper qnaCommentMapper;

    public Long createQnAComment(Long qnaId, CreateQnACommentRequest commentRequest) {
        QnA qna = checkQnAValidate(qnaId);

        Long writerId = getMemberId();
        Member member = findMemberById(writerId);

        QnAComment comment = qnaCommentMapper.toQnAComment(member, qna, commentRequest);

        return qnACommentRepository.save(comment).getId();
    }

    private QnA checkQnAValidate(Long qnaId) {
        return qnaRepository.findById(qnaId).orElseThrow(() -> new DataNotFoundException());
    }

    private Member findMemberById(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(() -> new DataNotFoundException());
    }
}
