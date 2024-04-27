package org.devridge.api.domain.coffeechat.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import org.devridge.api.common.dto.UserInformation;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class GetCoffeeChatRequestResponse {

    private Long id;
    private UserInformation member;
    private String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String status;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
