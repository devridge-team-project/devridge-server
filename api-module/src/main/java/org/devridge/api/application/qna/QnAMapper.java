package org.devridge.api.application.qna;

import org.devridge.api.common.dto.UserInformation;
import org.devridge.api.domain.member.entity.Member;
import org.devridge.api.domain.qna.dto.request.CreateQnARequest;
import org.devridge.api.domain.qna.dto.response.GetAllQnAResponse;
import org.devridge.api.domain.qna.dto.response.GetQnADetailResponse;
import org.devridge.api.domain.qna.entity.QnA;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.devridge.api.common.util.MemberUtil.toMember;

@Component
public class QnAMapper {

    public List<GetAllQnAResponse> toGetAllQnAResponses(List<QnA> qna) {
        List<GetAllQnAResponse> responses = new ArrayList<>();

        for (QnA q : qna) {
            String writerNickname = q.getMember().getNickname() + "님의 질문";

            responses.add(
                GetAllQnAResponse.builder()
                    .id(q.getId())
                    .title(q.getTitle())
                    .content(q.getContent())
                    .commentCount(q.getComments().size())
                    .views(q.getViews())
                    .nickname(writerNickname)
                    .likes(q.getLikes())
                    .createdAt(q.getCreatedAt())
                    .build()
            );
        }

        return responses;
    }

    public GetQnADetailResponse toGetQnADetailResponse(QnA result) {
        UserInformation member = toMember(result.getMember());

        return GetQnADetailResponse.builder()
            .member(member)
            .title(result.getTitle())
            .content(result.getContent())
            .likes(result.getLikes())
            .dislikes(result.getDislikes())
            .views(result.getViews() + 1)
            .createdAt(result.getCreatedAt())
            .commentCount(result.getComments().size())
            .build();
    }

    public QnA toQnA(CreateQnARequest qnaRequest, Member member) {
        QnA.QnABuilder qna = QnA.builder()
                .member(member)
                .title(qnaRequest.getTitle())
                .content(qnaRequest.getContent());

        if (qnaRequest.getImageUrl() != null && !qnaRequest.getImageUrl().isEmpty()) {
            String imageUrl = qnaRequest.getImageUrl().toString();
            qna.imageUrl(imageUrl.substring(1, imageUrl.length() - 1));
        }

        return qna.build();
    }
}
