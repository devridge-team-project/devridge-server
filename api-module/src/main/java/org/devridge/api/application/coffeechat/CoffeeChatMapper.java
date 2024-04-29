package org.devridge.api.application.coffeechat;

import org.devridge.api.common.dto.UserInformation;
import org.devridge.api.domain.coffeechat.dto.request.CreateChatMessageRequest;
import org.devridge.api.domain.coffeechat.dto.response.*;
import org.devridge.api.domain.coffeechat.entity.ChatMessage;
import org.devridge.api.domain.coffeechat.entity.ChatRoom;
import org.devridge.api.domain.coffeechat.entity.CoffeeChatRequest;
import org.devridge.api.domain.member.entity.Member;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.devridge.api.common.util.MemberUtil.toMember;

@Component
public class CoffeeChatMapper {

    public List<GetAllMyChatRoom> toGetAllMyChatRooms(
        List<ChatRoom> chatRooms,
        List<ChatMessage> messages,
        Member member
    ) {
        List<GetAllMyChatRoom> myChatRooms = new ArrayList<>();

        for (ChatRoom room : chatRooms) {
            Member otherMember = Objects.equals(room.getFirstMember().getId(), member.getId())
                ? room.getSecondMember()
                : room.getFirstMember();

            LastMessageInformation lastMessageInformation = this.getLastMessageInformation(
                messages,
                otherMember.getNickname(),
                room
            );

            myChatRooms.add(
                GetAllMyChatRoom.builder()
                    .id(room.getId())
                    .member(toMember(otherMember))
                    .lastMessage(lastMessageInformation)
                    .build()
            );
        }

        return myChatRooms;
    }

    private LastMessageInformation getLastMessageInformation(List<ChatMessage> messages, String nickname, ChatRoom room) {
        /* default information value */
        String lastMessage = nickname + "님과 즐거운 대화를 나눠보세요!";
        LocalDateTime createdAt = room.getCreatedAt();
        LocalDateTime updatedAt = room.getUpdatedAt();

        for (ChatMessage message : messages) {
            if (Objects.equals(room.getId(), message.getChatRoom().getId())) {
                lastMessage = message.getContent();
                createdAt = message.getCreatedAt();
                updatedAt = message.getUpdatedAt();
                break;
            }
        }

        return LastMessageInformation.builder()
            .message(lastMessage)
            .createdAt(createdAt)
            .updateAt(updatedAt)
            .build();
    }

    public List<GetAllChatMessage> toGetAllChatMessage(List<ChatMessage> chatMessages) {
        List<GetAllChatMessage> messages = new ArrayList<>();

        for (ChatMessage message : chatMessages) {
            messages.add(
                new GetAllChatMessage(
                    message.getId(),
                    toMember(message.getMember()),
                    message.getContent(),
                    message.getCreatedAt(),
                    message.getUpdatedAt()
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

        return GetCoffeeChatRequestResponse.builder()
            .id(request.getId())
            .member(fromMember)
            .message(request.getMessage())
            .createdAt(request.getCreatedAt())
            .updatedAt(request.getUpdatedAt())
            .build();
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
                GetCoffeeChatRequestResponse.builder()
                    .id(request.getId())
                    .member(toMember(request.getToMember()))
                    .message(request.getMessage())
                    .status(status)
                    .createdAt(request.getCreatedAt())
                    .updatedAt(request.getUpdatedAt())
                    .build()
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
                GetCoffeeChatRequestResponse.builder()
                    .id(request.getId())
                    .member(toMember(request.getFromMember()))
                    .message(request.getMessage())
                    .createdAt(request.getCreatedAt())
                    .updatedAt(request.getUpdatedAt())
                    .build()
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

        return new GetAllChatMessage(
            message.getId(),
            member,
            message.getContent(),
            message.getCreatedAt(),
            message.getUpdatedAt()
        );
    }
}
