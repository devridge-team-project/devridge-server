package org.devridge.api.domain.qna.mapper;

import org.devridge.api.domain.member.entity.Member;
import org.devridge.api.domain.qna.dto.request.CreateQnACommentRequest;
import org.devridge.api.domain.qna.entity.QnA;
import org.devridge.api.domain.qna.entity.QnAComment;

import org.springframework.stereotype.Component;

@Component
public class QnACommentMapper {

    public QnAComment toQnAComment(Member member, QnA qna, CreateQnACommentRequest commentRequest) {
        return QnAComment.builder()
            .member(member)
            .qna(qna)
            .content(commentRequest.getContent())
            .build();
    }
}
