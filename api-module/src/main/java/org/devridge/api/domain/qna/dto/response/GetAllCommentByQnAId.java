package org.devridge.api.domain.qna.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class GetAllCommentByQnAId {

    private Long id;
    private FindWriterInformation member;
    private String content;
    private Integer likes;
    private Integer dislikes;
    private LocalDateTime createdAt;
}
