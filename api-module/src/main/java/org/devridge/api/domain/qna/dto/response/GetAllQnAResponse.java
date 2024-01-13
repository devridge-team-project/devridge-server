package org.devridge.api.domain.qna.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class GetAllQnAResponse {

    private Long id;
    private String title;
    private Integer views;
    private Integer commentCount;
}
