package org.devridge.api.domain.coffeechat.controller;

import lombok.RequiredArgsConstructor;

import org.devridge.api.domain.coffeechat.dto.request.CreateChatMessageRequest;
import org.devridge.api.domain.coffeechat.dto.response.GetAllChatMessage;
import org.devridge.api.domain.coffeechat.service.CoffeeChatService;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class ChatStompController {

    private final CoffeeChatService coffeeChatService;

    /**
     * 채팅 메세지 보내기
     * @param roomId
     * @param request
     */
    @MessageMapping("/{roomId}")     // request: /api/coffee-chat/publish/{roomId}
    @SendTo("/api/coffee-chat/subscribe/{roomId}")  // subscribe 채팅방으로 메세지 전송
    public ResponseEntity<GetAllChatMessage> createChatMessage(
        @DestinationVariable Long roomId,
        @RequestBody @Valid CreateChatMessageRequest request
    ) {
        GetAllChatMessage message = coffeeChatService.createChatMessage(request, roomId);
        return ResponseEntity.ok().body(message);
    }
}
