package org.devridge.api.domain.qna.dto.response;

import lombok.Builder;
import lombok.Getter;

import org.devridge.api.domain.member.entity.Member;
import org.devridge.api.domain.qna.entity.QnAComment;

import java.time.LocalDateTime;

import java.util.List;

@Getter
@Builder
public class GetQnADetailResponse {

    private FindWriterInformation member;
    private String title;
    private String content;
    private Integer views;
    private Integer likes;
    private Integer dislikes;
    private LocalDateTime createdAt;
    private Integer commentCount;
    // TODO: 댓글 무한 스크롤이 필요한지 확인
    private List<QnAComment> comments;
}
