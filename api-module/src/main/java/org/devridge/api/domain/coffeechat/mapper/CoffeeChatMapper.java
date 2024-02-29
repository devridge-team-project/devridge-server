package org.devridge.api.domain.coffeechat.mapper;

import org.devridge.api.domain.coffeechat.dto.request.CreateCoffeeChatRequest;
import org.devridge.api.domain.coffeechat.dto.response.GetAllChatMessage;
import org.devridge.api.domain.coffeechat.dto.response.GetAllMyChatRoom;
import org.devridge.api.domain.coffeechat.entity.ChatMessage;
import org.devridge.api.domain.coffeechat.entity.ChatRoom;
import org.devridge.api.domain.coffeechat.entity.CoffeeChatRequest;
import org.devridge.api.domain.member.entity.Member;

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
            messages.add(new GetAllChatMessage(
                message.getId(),
                toMember(message.getMember()),
                message.getContent())
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
}
