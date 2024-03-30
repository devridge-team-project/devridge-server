package org.devridge.api.domain.coffeechat.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;

import org.devridge.api.common.dto.UserInformation;

@Getter
public class GetCoffeeChatRequestResponse {

    private Long id;
    private UserInformation member;
    private String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String status;

    public GetCoffeeChatRequestResponse(Long id, UserInformation member, String message) {
        this.id = id;
        this.member = member;
        this.message = message;
    }

    public GetCoffeeChatRequestResponse(Long id, UserInformation member, String message, String status) {
        this.id = id;
        this.member = member;
        this.message = message;
        this.status = status;
    }
}
