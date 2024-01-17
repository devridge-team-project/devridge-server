package org.devridge.api.domain.qna.mapper;

import org.devridge.api.domain.qna.dto.request.CreateQnARequest;
import org.devridge.api.domain.qna.dto.response.GetAllQnAResponse;
import org.devridge.api.domain.qna.dto.response.GetQnADetailResponse;
import org.devridge.api.domain.qna.entity.QnA;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class QnAMapper {

    public GetQnADetailResponse toGetQnADetailResponse(QnA result) {
        return GetQnADetailResponse.builder()
            .memberId(result.getMemberId())
            .title(result.getTitle())
            .content(result.getContent())
            .likes(result.getLikes())
            .dislikes(result.getDislikes())
            .views(result.getViews())
            .commentCount(result.getComments().size())
            .comments(result.getComments())
            .build();
    }

    public QnA toQnA(CreateQnARequest qnaRequest) {
        return QnA.builder()
            .title(qnaRequest.getTitle())
            .content(qnaRequest.getContent())
            .build();
    }
}
