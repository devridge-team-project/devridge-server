package org.devridge.api.domain.coffeechat.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import org.devridge.api.common.dto.FindWriterInformation;

@Getter
@AllArgsConstructor
public class GetAllChatMessage {

    private Long id;
    private FindWriterInformation member;
    private String content;
}
