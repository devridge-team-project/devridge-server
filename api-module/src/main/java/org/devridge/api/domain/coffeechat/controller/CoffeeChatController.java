package org.devridge.api.domain.coffeechat.controller;

import lombok.RequiredArgsConstructor;
import org.devridge.api.domain.coffeechat.dto.response.GetAllMyChatRoom;
import org.devridge.api.domain.coffeechat.service.CoffeeChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/coffee-chat")
public class CoffeeChatController {

    private final CoffeeChatService coffeeChatService;

    @GetMapping
    public ResponseEntity<List<GetAllMyChatRoom>> getAllMyChatRoom(
        @RequestParam(value = "lastIndex", required = false) Long lastIndex
    ) {
        List<GetAllMyChatRoom> result = coffeeChatService.getAllMyChatRoom(lastIndex);
        return ResponseEntity.ok().body(result);
    }
}
