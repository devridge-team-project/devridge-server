package org.devridge.api.domain.coffeechat.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class LastMessageInformation {

    private String message;
    private LocalDateTime createdAt;
    private LocalDateTime updateAt;
}
