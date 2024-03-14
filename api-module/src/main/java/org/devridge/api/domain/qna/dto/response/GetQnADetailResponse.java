package org.devridge.api.domain.qna.dto.response;

import lombok.Builder;
import lombok.Getter;
import org.devridge.api.common.dto.UserInformation;

import java.time.LocalDateTime;

@Getter
@Builder
public class GetQnADetailResponse {

    private UserInformation member;
    private String title;
    private String content;
    private Integer views;
    private Integer likes;
    private Integer dislikes;
    private LocalDateTime createdAt;
    private Integer commentCount;
}
