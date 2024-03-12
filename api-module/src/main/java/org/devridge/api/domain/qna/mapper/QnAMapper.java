package org.devridge.api.domain.qna.mapper;

import org.devridge.api.domain.member.entity.Member;
import org.devridge.api.domain.qna.dto.request.CreateQnARequest;
import org.devridge.api.common.dto.FindWriterInformation;
import org.devridge.api.domain.qna.dto.response.GetQnADetailResponse;
import org.devridge.api.domain.qna.entity.QnA;

import org.springframework.stereotype.Component;

import static org.devridge.api.common.util.MemberUtil.toMember;

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
            .build();
    }

    public QnA toQnA(CreateQnARequest qnaRequest, Member member) {
        if (qnaRequest.getImageUrl() != null && !qnaRequest.getImageUrl().isEmpty()) {
            String imageUrl = qnaRequest.getImageUrl().toString();
            return QnA.builder()
                .member(member)
                .title(qnaRequest.getTitle())
                .content(qnaRequest.getContent())
                .imageUrl(imageUrl.substring(1, imageUrl.length() - 1))
                .build();
        }

        return QnA.builder()
            .member(member)
            .title(qnaRequest.getTitle())
            .content(qnaRequest.getContent())
            .build();
    }
}
