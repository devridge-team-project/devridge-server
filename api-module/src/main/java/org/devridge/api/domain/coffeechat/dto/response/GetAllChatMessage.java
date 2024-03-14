package org.devridge.api.domain.coffeechat.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import org.devridge.api.common.dto.UserInformation;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class GetAllChatMessage {

    private Long id;
    private UserInformation member;
    private String message;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
