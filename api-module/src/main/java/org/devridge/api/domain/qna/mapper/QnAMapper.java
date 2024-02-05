package org.devridge.api.domain.qna.mapper;

import org.devridge.api.domain.member.entity.Member;
import org.devridge.api.domain.qna.dto.request.CreateQnARequest;
import org.devridge.api.domain.qna.dto.response.FindWriterInformation;
import org.devridge.api.domain.qna.dto.response.GetAllCommentByQnAId;
import org.devridge.api.domain.qna.dto.response.GetQnADetailResponse;
import org.devridge.api.domain.qna.entity.QnA;
import org.devridge.api.domain.qna.entity.QnAComment;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.devridge.api.domain.qna.mapper.MemberMapper.toMember;

@Component
public class QnAMapper {

    public GetQnADetailResponse toGetQnADetailResponse(QnA result) {
        FindWriterInformation member = toMember(result.getMember());

        return GetQnADetailResponse.builder()
            .member(member)
            .title(result.getTitle())
            .content(result.getContent())
            .likes(result.getLikes())
            .dislikes(result.getDislikes())
            .views(result.getViews() + 1)
            .createdAt(result.getCreatedAt())
            .commentCount(result.getComments().size())
            .comments(this.toComments(result.getComments()))
            .build();
    }

    public QnA toQnA(CreateQnARequest qnaRequest, Member member) {
        return QnA.builder()
            .member(member)
            .title(qnaRequest.getTitle())
            .content(qnaRequest.getContent())
            .build();
    }

    private List<GetAllCommentByQnAId> toComments(List<QnAComment> comments) {
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
