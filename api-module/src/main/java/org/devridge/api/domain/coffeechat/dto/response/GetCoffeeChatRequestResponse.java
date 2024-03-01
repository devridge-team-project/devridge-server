package org.devridge.api.domain.coffeechat.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import org.devridge.common.dto.FindWriterInformation;

@Builder
@AllArgsConstructor
@Getter
public class GetCoffeeChatRequestResponse {

    private Long id;
    private FindWriterInformation member;
    private String message;
}
