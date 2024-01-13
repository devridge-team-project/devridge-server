package org.devridge.api.domain.qna.mapper;

import org.devridge.api.domain.qna.dto.response.GetAllQnAResponse;
import org.devridge.api.domain.qna.entity.QnA;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class QnAMapper {

    public List<GetAllQnAResponse> toGetAllQnAResponse(List<QnA> results) {
        List<GetAllQnAResponse> convertResult = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            QnA qna = results.get(i);

            convertResult.add(
                GetAllQnAResponse.builder()
                    .id(qna.getId())
                    .title(qna.getTitle())
               //     .likes(qna.getLikes())
                    .views(qna.getViews())
                    .commentCount(qna.getComments().size())
                    .build()
            );
        }

        return convertResult;
    }
}
