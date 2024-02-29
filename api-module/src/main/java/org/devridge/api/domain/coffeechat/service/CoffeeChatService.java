package org.devridge.api.domain.coffeechat.service;

import lombok.RequiredArgsConstructor;

import org.devridge.api.domain.coffeechat.dto.response.GetAllMyChatRoom;
import org.devridge.api.domain.coffeechat.entity.ChatRoom;
import org.devridge.api.domain.coffeechat.mapper.CoffeeChatMapper;
import org.devridge.api.domain.coffeechat.repository.ChatRoomRepository;
import org.devridge.api.domain.coffeechat.repository.CoffeeChatQuerydslRepository;
import org.devridge.api.domain.member.entity.Member;
import org.devridge.api.domain.member.repository.MemberRepository;
import org.devridge.api.exception.common.DataNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.devridge.api.util.SecurityContextHolderUtil.getMemberId;

@RequiredArgsConstructor
@Service
public class CoffeeChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final MemberRepository memberRepository;
    private final CoffeeChatQuerydslRepository coffeeChatQuerydslRepository;
    private final CoffeeChatMapper coffeeChatMapper;

    public List<GetAllMyChatRoom> getAllMyChatRoom(Long lastIndex) {
        Member member = this.getMember();

        if (lastIndex == null) {
            Long maxId = chatRoomRepository.findMaxId(member).orElse(0L);
            List<ChatRoom> chatRooms = coffeeChatQuerydslRepository.findAllMyChatRoomByMemberId(maxId, member);
            return coffeeChatMapper.toGetAllMyChatRooms(chatRooms, member);
        }

        List<ChatRoom> chatRooms = coffeeChatQuerydslRepository.findAllMyChatRoomByMemberId(lastIndex, member);
        return coffeeChatMapper.toGetAllMyChatRooms(chatRooms, member);
    }

    private Member getMember() {
        Long memberId = getMemberId();
        return memberRepository.findById(memberId).orElseThrow(() -> new DataNotFoundException());
    }
}
