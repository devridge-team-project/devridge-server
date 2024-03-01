package org.devridge.api.domain.coffeechat.dto.request;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class CreateChatMessageRequest {

    @NotBlank(message = "메세지를 입력해주세요.")
    private String message;
}
