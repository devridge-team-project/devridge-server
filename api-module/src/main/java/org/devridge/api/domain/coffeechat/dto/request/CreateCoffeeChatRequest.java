package org.devridge.api.domain.coffeechat.dto.request;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class CreateCoffeeChatRequest {

    private Long toMemberId;

    @NotBlank(message = "커피챗 요청 시 메세지를 입력해주세요.")
    private String message;
}
