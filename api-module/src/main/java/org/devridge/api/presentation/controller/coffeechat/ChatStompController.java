package org.devridge.api.presentation.controller.coffeechat;

import lombok.RequiredArgsConstructor;

import org.devridge.api.domain.coffeechat.dto.request.CreateChatMessageRequest;
import org.devridge.api.domain.coffeechat.dto.response.GetAllChatMessage;
import org.devridge.api.application.coffeechat.CoffeeChatService;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@RequiredArgsConstructor
@Controller
public class ChatStompController {

    private final CoffeeChatService coffeeChatService;
    private final SimpMessagingTemplate template;

    /**
     * 채팅 메세지 보내기
     * @param roomId
     * @param request
     */
    @MessageMapping("/{roomId}")     // request: /api/pub/{roomId}
    @SendTo("/api/sub/{roomId}")  // subscribe 채팅방으로 메세지 전송
    public void createChatMessage(
        @DestinationVariable Long roomId,
        @Payload CreateChatMessageRequest request,
        SimpMessageHeaderAccessor accessor
    ) {
        String email = (String) accessor.getSessionAttributes().get("senderEmail");
        GetAllChatMessage message = coffeeChatService.createChatMessage(request, roomId, email);
        template.convertAndSend("/api/sub/" + roomId, message);
    }
}
