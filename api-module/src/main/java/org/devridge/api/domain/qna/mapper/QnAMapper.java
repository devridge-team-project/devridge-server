package org.devridge.api.domain.qna.mapper;

import org.devridge.api.domain.member.entity.Member;
import org.devridge.api.domain.qna.dto.request.CreateQnARequest;
import org.devridge.api.domain.qna.dto.response.FindWriterInformation;
import org.devridge.api.domain.qna.dto.response.GetQnADetailResponse;
import org.devridge.api.domain.qna.entity.QnA;

import org.springframework.stereotype.Component;

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
            .views(result.getViews())
            .createdAt(result.getCreatedAt())
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

    private FindWriterInformation toMember(Member member) {
        return FindWriterInformation.builder()
            .id(member.getId())
            .nickname(member.getNickname())
            .introduction(member.getIntroduction())
            .profileImageUrl(member.getProfileImageUrl())
            .build();
    }
}
