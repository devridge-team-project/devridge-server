package org.devridge.api.domain.qna.dto.request;

import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
public class UpdateQnARequest {

    // TODO: 추후 코드, 이미지 처리 필요
    @Size(min = 5, max = 50)
    @NotBlank(message = "제목 값은 필수입니다.")
    private String title;

    @NotBlank(message = "내용을 입력해주세요.")
    private String content;
}
