package org.devridge.api.domain.coffeechat.dto.response;

import lombok.Getter;

@Getter
public class CoffeeChatResult {

    private String result;

    public CoffeeChatResult(String result) {
        this.result = result;
    }
}
