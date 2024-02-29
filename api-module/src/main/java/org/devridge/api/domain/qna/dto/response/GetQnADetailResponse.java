package org.devridge.api.domain.qna.dto.response;

import lombok.Builder;
import lombok.Getter;
import org.devridge.common.dto.FindWriterInformation;

import java.time.LocalDateTime;

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
