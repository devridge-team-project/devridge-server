package org.devridge.api.infrastructure.coffeechat;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

import org.devridge.api.domain.coffeechat.entity.*;
import org.devridge.api.domain.member.entity.Member;

import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class CoffeeChatQuerydslRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final int PAGE_SIZE = 10;

    private QChatRoom qChatRoom = QChatRoom.chatRoom;
    private QChatMessage qChatMessage = QChatMessage.chatMessage;
    private QCoffeeChatRequest qCoffeeChatRequest = QCoffeeChatRequest.coffeeChatRequest;

    public List<ChatRoom> findAllMyChatRoomByMemberId(Long lastIndex, Member member) {
        return jpaQueryFactory
            .selectFrom(qChatRoom)
            .where(
                qChatRoom.firstMember.eq(member)
                .or(qChatRoom.secondMember.eq(member)),
                qChatRoom.id.loe(lastIndex)
            )
            .limit(PAGE_SIZE)
            .fetch();
    }

    public List<ChatMessage> findLastChatMessageByChatRoomId(ChatRoom chatRoom) {
        return jpaQueryFactory
            .selectFrom(qChatMessage)
            .where(qChatMessage.chatRoom.eq(chatRoom))
            .orderBy(qChatMessage.id.desc())
            .limit(1)
            .fetch();
    }

    public List<ChatMessage> findAllChatMessagesByChatRoomId(Long lastIndex, ChatRoom chatRoom) {
        return jpaQueryFactory
            .selectFrom(qChatMessage)
            .where(
                qChatMessage.chatRoom.eq(chatRoom),
                qChatMessage.id.loe(lastIndex)
            )
            .limit(PAGE_SIZE)
            .fetch();
    }

    /**
     * 내가 보낸 요청 목록 리스트 (fromMember가 자기 자신)
     * @param toMember
     */
    public List<CoffeeChatRequest> findAllSendCoffeeChatRequest(Member toMember, Long lastIndex) {
        return jpaQueryFactory
            .selectFrom(qCoffeeChatRequest)
            .where(
                qCoffeeChatRequest.fromMember.eq(toMember),
                qCoffeeChatRequest.id.loe(lastIndex)
            )
            .limit(PAGE_SIZE)
            .fetch();
    }

    /**
     * 내가 받은 요청 목록 리스트 (toMember가 자기 자신)
     * @param fromMember
     */
    public List<CoffeeChatRequest> findAllReceiveCoffeeChatRequest(Member fromMember, Long lastIndex) {
        return jpaQueryFactory
            .selectFrom(qCoffeeChatRequest)
            .where(
                qCoffeeChatRequest.toMember.eq(fromMember),
                qCoffeeChatRequest.id.loe(lastIndex),
                qCoffeeChatRequest.isSuccess.isNull()
            )
            .limit(PAGE_SIZE)
            .fetch();
    }
}
