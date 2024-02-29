package org.devridge.api.domain.coffeechat.dto.response;

import lombok.Getter;
import org.devridge.api.domain.coffeechat.dto.type.YesOrNo;

@Getter
public class CoffeeChatResult {

    private String result;

    public CoffeeChatResult(String result) {
        this.result = result;
    }
}
