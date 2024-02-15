package org.devridge.api.domain.qna.dto.response;

import lombok.Builder;
import lombok.Getter;

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
}
