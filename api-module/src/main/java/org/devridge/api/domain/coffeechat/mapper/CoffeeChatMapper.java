package org.devridge.api.domain.coffeechat.mapper;

import org.devridge.api.domain.coffeechat.dto.request.CreateChatMessageRequest;
import org.devridge.api.domain.coffeechat.dto.response.*;
import org.devridge.api.domain.coffeechat.entity.ChatMessage;
import org.devridge.api.domain.coffeechat.entity.ChatRoom;
import org.devridge.api.domain.coffeechat.entity.CoffeeChatRequest;
import org.devridge.api.domain.member.entity.Member;
import org.devridge.common.dto.FindWriterInformation;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.devridge.api.util.MemberUtil.toMember;

@Component
public class CoffeeChatMapper {

    public List<GetAllMyChatRoom> toGetAllMyChatRooms(List<ChatRoom> chatRooms, Member member) {
        List<GetAllMyChatRoom> myChatRooms = new ArrayList<>();

        for (ChatRoom room : chatRooms) {
            String title = Objects.equals(room.getFirstMember().getId(), member.getId())
                    ? room.getSecondMember().getNickname()
                    : room.getFirstMember().getNickname();

            myChatRooms.add(new GetAllMyChatRoom(room.getId(), title));
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
        FindWriterInformation fromMember = toMember(request.getFromMember());
        return new GetCoffeeChatRequestResponse(request.getId(), fromMember, request.getMessage());
    }

    public GetAllCoffeeChatRequest toGetSendCoffeeChatRequests(List<CoffeeChatRequest> requests) {
        List<GetCoffeeChatRequestResponse> coffeeChatRequests = new ArrayList<>();
        Integer noReadCount = 0;

        for (CoffeeChatRequest request : requests) {
            if (request.getReadAt() == null) {
                noReadCount++;
            }

            coffeeChatRequests.add(
                new GetCoffeeChatRequestResponse(
                    request.getId(),
                    toMember(request.getToMember()),
                    request.getMessage()
                )
            );
        }

        return new GetAllCoffeeChatRequest(coffeeChatRequests, noReadCount);
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
}
