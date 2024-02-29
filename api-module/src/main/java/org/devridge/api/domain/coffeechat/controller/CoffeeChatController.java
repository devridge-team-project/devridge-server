package org.devridge.api.domain.coffeechat.controller;

import lombok.RequiredArgsConstructor;
import org.devridge.api.domain.coffeechat.dto.response.GetAllChatMessage;
import org.devridge.api.domain.coffeechat.dto.response.GetAllMyChatRoom;
import org.devridge.api.domain.coffeechat.service.CoffeeChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/coffee-chat")
public class CoffeeChatController {

    private final CoffeeChatService coffeeChatService;

    /**
     * 내 전체 커피챗 리스트 조회
     * @param lastIndex
     * @return 내 채팅방 리스트 목록 무한 스크롤
     */
    @GetMapping
    public ResponseEntity<List<GetAllMyChatRoom>> getAllMyChatRoom(
        @RequestParam(value = "lastIndex", required = false) Long lastIndex
    ) {
        List<GetAllMyChatRoom> result = coffeeChatService.getAllMyChatRoom(lastIndex);
        return ResponseEntity.ok().body(result);
    }

    /**
     * 채팅방 입장 시 메세지 리스트 조회
     * @param chatRoomId
     * @param lastIndex
     * @return 해당 채팅방 메세지 목록 무한 스크롤
     */
    @GetMapping("/{chatRoomId}")
    public ResponseEntity<List<GetAllChatMessage>> getAllChatMessages(
        @PathVariable Long chatRoomId,
        @RequestParam(value = "lastIndex", required = false) Long lastIndex
    ) {
        List<GetAllChatMessage> result = coffeeChatService.getAllChatMessage(chatRoomId, lastIndex);
        return ResponseEntity.ok().body(result);
    }
}
