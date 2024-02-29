package org.devridge.api.domain.coffeechat.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

import org.devridge.api.domain.coffeechat.entity.ChatRoom;
import org.devridge.api.domain.coffeechat.entity.QChatRoom;
import org.devridge.api.domain.member.entity.Member;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class CoffeeChatQuerydslRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final int PAGE_SIZE = 10;

    private QChatRoom qChatRoom = QChatRoom.chatRoom;

    public List<ChatRoom> findAllMyChatRoomByMemberId(Long lastIndex, Member member) {
        return jpaQueryFactory
            .selectFrom(qChatRoom)
            .where(qChatRoom.firstMember.eq(member).or(qChatRoom.secondMember.eq(member)))
            .limit(PAGE_SIZE)
            .fetch();
    }
}
