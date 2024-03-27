package org.devridge.api.domain.qna.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import com.querydsl.core.annotations.QueryProjection;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class GetAllQnAResponse {

    private Long id;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String nickname;
    private String title;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String content;
    private Integer likes;
    private Integer views;
    private Integer commentCount;
    private LocalDateTime createdAt;

    @QueryProjection
    public GetAllQnAResponse(
        Long id,
        String title,
        Integer likes,
        Integer views,
        Integer commentCount,
        LocalDateTime createdAt
    ) {
        this.id = id;
        this.title = title;
        this.likes = likes;
        this.views = views;
        this.commentCount = commentCount;
        this.createdAt = createdAt;
    }

    @Builder
    public GetAllQnAResponse(
        Long id,
        String title,
        String content,
        Integer likes,
        Integer views,
        Integer commentCount,
        String nickname,
        LocalDateTime createdAt
    ) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.nickname = nickname;
        this.likes = likes;
        this.views = views;
        this.commentCount = commentCount;
        this.createdAt = createdAt;
    }
}
