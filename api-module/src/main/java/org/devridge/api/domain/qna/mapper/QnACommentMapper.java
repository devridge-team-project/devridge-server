package org.devridge.api.domain.qna.mapper;

import org.devridge.api.domain.member.entity.Member;
import org.devridge.api.domain.qna.dto.request.CreateQnACommentRequest;
import org.devridge.api.domain.qna.dto.response.GetAllCommentByQnAId;
import org.devridge.api.domain.qna.entity.QnA;
import org.devridge.api.domain.qna.entity.QnAComment;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.devridge.api.domain.qna.mapper.MemberMapper.toMember;

@Component
public class QnACommentMapper {

    public QnAComment toQnAComment(Member member, QnA qna, CreateQnACommentRequest commentRequest) {
        return QnAComment.builder()
            .member(member)
            .qna(qna)
            .content(commentRequest.getContent())
            .build();
    }

    public List<GetAllCommentByQnAId> toQnAComments(List<QnAComment> comments) {
        List<GetAllCommentByQnAId> result = new ArrayList<>();

        for (QnAComment comment : comments) {
            result.add(
                GetAllCommentByQnAId.builder()
                    .id(comment.getId())
                    .member(toMember(comment.getMember()))
                    .content(comment.getContent())
                    .likes(comment.getLikes())
                    .dislikes(comment.getDislikes())
                    .createdAt(comment.getCreatedAt())
                    .build()
            );
        }

        return result;
    }
}
