package org.devridge.api.domain.coffeechat.controller;

import lombok.RequiredArgsConstructor;

import org.devridge.api.domain.coffeechat.dto.request.AcceptOrRejectCoffeeChatRequest;
import org.devridge.api.domain.coffeechat.dto.request.CreateChatMessageRequest;
import org.devridge.api.domain.coffeechat.dto.request.CreateCoffeeChatRequest;
import org.devridge.api.domain.coffeechat.dto.response.*;
import org.devridge.api.domain.coffeechat.service.CoffeeChatService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.net.URI;

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

    /**
     * 커피챗 요청 생성
     */
    @PostMapping("/request")
    public ResponseEntity<Void> createCoffeeChatRequest(
        @RequestBody @Valid CreateCoffeeChatRequest coffeeChatRequest
    ) {
        Long coffeeChatRequestId = coffeeChatService.createCoffeeChatRequest(coffeeChatRequest);
        return ResponseEntity.created(URI.create("/api/coffee-chat/request" + coffeeChatRequestId)).build();
    }

    /**
     * 커피챗 요청 리스트(커피챗 목록 + 알림으로 보여주기 위한 readAt이 null인 개수 반환)
     */
    @GetMapping("/request")
    public ResponseEntity<GetAllCoffeeChatRequest> getCoffeeChatRequests(
        @RequestParam(value = "viewOption") String viewOption
    ) {
        GetAllCoffeeChatRequest result = coffeeChatService.getCoffeeChatRequests(viewOption);
        return ResponseEntity.ok().body(result);
    }

    /**
     * 커피챗 요청 보기
     * @param requestId
     * @return 커피챗 요청 세부 내용
     */
    @GetMapping("/request/{requestId}")
    public ResponseEntity<GetCoffeeChatRequestResponse> getCoffeeChat(@PathVariable Long requestId) {
        GetCoffeeChatRequestResponse result = coffeeChatService.getCoffeeChatRequest(requestId);
        return ResponseEntity.ok().body(result);
    }

    /**
     * 커피챗 승인 및 거절
     * @param requestId
     * @param request
     * @return 승인 및 거절 결과
     */
    @PatchMapping("/request/{requestId}")
    public ResponseEntity<CoffeeChatResult> acceptOrRejectCoffeeChatRequest(
        @PathVariable Long requestId,
        @RequestBody @Valid AcceptOrRejectCoffeeChatRequest request
    ) {
        CoffeeChatResult result = coffeeChatService.acceptOrRejectCoffeeChatRequest(requestId, request);
        return ResponseEntity.ok().body(result);
    }

    /**
     * 채팅 메세지 보내기
     * @param roomId
     * @param request
     */
    @PostMapping("/{roomId}")
    public ResponseEntity<Void> createChatMessage(
        @PathVariable Long roomId,
        @RequestBody @Valid CreateChatMessageRequest request
    ) {
        Long messageId = coffeeChatService.createChatMessage(request, roomId);
        return ResponseEntity.created(URI.create("/api/coffee-chat/" + roomId + "/" + messageId)).build();
    }

    /**
     * 채팅 메세지 삭제
     * @param roomId
     * @param messageId
     */
    @DeleteMapping("/{roomId}/{messageId}")
    public ResponseEntity<Void> deleteChatMessage(
        @PathVariable Long roomId,
        @PathVariable Long messageId
    ) {
        coffeeChatService.deleteChatMessage(roomId, messageId);
        return ResponseEntity.ok().build();
    }
}
