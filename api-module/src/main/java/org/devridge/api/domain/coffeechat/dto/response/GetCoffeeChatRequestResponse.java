package org.devridge.api.domain.coffeechat.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;

import org.devridge.common.dto.FindWriterInformation;

@Getter
public class GetCoffeeChatRequestResponse {

    private Long id;
    private FindWriterInformation member;
    private String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String status;

    public GetCoffeeChatRequestResponse(Long id, FindWriterInformation member, String message) {
        this.id = id;
        this.member = member;
        this.message = message;
    }

    public GetCoffeeChatRequestResponse(Long id, FindWriterInformation member, String message, String status) {
        this.id = id;
        this.member = member;
        this.message = message;
        this.status = status;
    }
}
