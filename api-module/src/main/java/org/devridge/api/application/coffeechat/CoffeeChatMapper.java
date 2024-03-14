package org.devridge.api.application.coffeechat;

import org.devridge.api.common.dto.UserInformation;
import org.devridge.api.domain.coffeechat.dto.request.CreateChatMessageRequest;
import org.devridge.api.domain.coffeechat.dto.response.*;
import org.devridge.api.domain.coffeechat.entity.ChatMessage;
import org.devridge.api.domain.coffeechat.entity.ChatRoom;
import org.devridge.api.domain.coffeechat.entity.CoffeeChatRequest;
import org.devridge.api.domain.member.entity.Member;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.devridge.api.common.util.MemberUtil.toMember;

@Component
public class CoffeeChatMapper {

    public List<GetAllMyChatRoom> toGetAllMyChatRooms(List<ChatRoom> chatRooms, Member member) {
        List<GetAllMyChatRoom> myChatRooms = new ArrayList<>();

        for (ChatRoom room : chatRooms) {
            Member otherMember = Objects.equals(room.getFirstMember().getId(), member.getId())
                    ? room.getSecondMember()
                    : room.getFirstMember();

            myChatRooms.add(new GetAllMyChatRoom(room.getId(), toMember(otherMember)));
        }

        return myChatRooms;
    }

    public List<GetAllChatMessage> toGetAllChatMessage(List<ChatMessage> chatMessages) {
        List<GetAllChatMessage> messages = new ArrayList<>();

        for (ChatMessage message : chatMessages) {
            messages.add(
                new GetAllChatMessage(
                    message.getId(),
                    toMember(message.getMember()),
                    message.getContent()
                )
            );
        }

        return messages;
    }

    public CoffeeChatRequest toCoffeeChatRequest(Member toMember, Member fromMember, String message) {
        return new CoffeeChatRequest(fromMember, toMember, message);
    }

    public ChatRoom toChatRoom(Member firstMember, Member secondMember) {
        return new ChatRoom(firstMember, secondMember);
    }

    public GetCoffeeChatRequestResponse toGetCoffeeChatRequest(CoffeeChatRequest request) {
        UserInformation fromMember = toMember(request.getFromMember());
        return new GetCoffeeChatRequestResponse(request.getId(), fromMember, request.getMessage());
    }

    public GetAllCoffeeChatRequest toGetSendCoffeeChatRequests(List<CoffeeChatRequest> requests) {
        List<GetCoffeeChatRequestResponse> coffeeChatRequests = new ArrayList<>();
        Integer noReadCount = 0;

        for (CoffeeChatRequest request : requests) {
            if (request.getReadAt() == null) {
                noReadCount++;
            }

            String status = this.getStatus(request.getIsSuccess());

            coffeeChatRequests.add(
                new GetCoffeeChatRequestResponse(
                    request.getId(),
                    toMember(request.getToMember()),
                    request.getMessage(),
                    status
                )
            );
        }

        return new GetAllCoffeeChatRequest(coffeeChatRequests, noReadCount);
    }

    private String getStatus(Boolean isSuccess) {
        if (isSuccess == null) {
            return "대기중";
        }

        if (isSuccess) {
            return "승인";
        }

        return "거절";
    }

    public GetAllCoffeeChatRequest toGetReceiveCoffeeChatRequests(List<CoffeeChatRequest> requests) {
        List<GetCoffeeChatRequestResponse> coffeeChatRequests = new ArrayList<>();
        Integer noReadCount = 0;

        for (CoffeeChatRequest request : requests) {
            if (request.getReadAt() == null) {
                noReadCount++;
            }

            coffeeChatRequests.add(
                new GetCoffeeChatRequestResponse(
                    request.getId(),
                    toMember(request.getFromMember()),
                    request.getMessage()
                )
            );
        }

        return new GetAllCoffeeChatRequest(coffeeChatRequests, noReadCount);
    }

    public ChatMessage toChatMessage(CreateChatMessageRequest request, Member member, ChatRoom chatRoom) {
        return ChatMessage.builder()
            .chatRoom(chatRoom)
            .member(member)
            .content(request.getMessage())
            .build();
    }

    public GetAllChatMessage toGetChatMessage(ChatMessage message) {
        UserInformation member = toMember(message.getMember());
        return new GetAllChatMessage(message.getId(), member, message.getContent());
    }
}
