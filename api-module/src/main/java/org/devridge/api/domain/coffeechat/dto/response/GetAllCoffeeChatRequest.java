package org.devridge.api.domain.coffeechat.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class GetAllCoffeeChatRequest {

    private List<GetCoffeeChatRequestResponse> coffeeChatRequests;
    private Long noReadCount;
}
