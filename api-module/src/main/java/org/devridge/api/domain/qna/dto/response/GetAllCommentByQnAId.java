package org.devridge.api.domain.qna.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class GetAllCommentByQnAId {

    private FindWriterInformation member;
    private String content;
    private Integer likes;
    private Integer dislikes;
    private LocalDateTime createdAt;
}
