package org.devridge.api.domain.coffeechat.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import org.devridge.api.common.dto.UserInformation;

@Getter
@Builder
@AllArgsConstructor
public class GetAllMyChatRoom {

    private Long id;
    private UserInformation member;
    private LastMessageInformation lastMessage;
}
