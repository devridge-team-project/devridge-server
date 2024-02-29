package org.devridge.api.domain.coffeechat.mapper;

import org.devridge.api.domain.coffeechat.dto.response.GetAllMyChatRoom;
import org.devridge.api.domain.coffeechat.entity.ChatRoom;
import org.devridge.api.domain.member.entity.Member;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
}
