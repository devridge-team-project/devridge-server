package org.devridge.api.application.coffeechat;

import lombok.RequiredArgsConstructor;

import org.devridge.api.domain.coffeechat.dto.request.AcceptOrRejectCoffeeChatRequest;
import org.devridge.api.domain.coffeechat.dto.request.CreateChatMessageRequest;
import org.devridge.api.domain.coffeechat.dto.request.CreateCoffeeChatRequest;
import org.devridge.api.domain.coffeechat.dto.response.*;
import org.devridge.api.domain.coffeechat.dto.type.ViewOption;
import org.devridge.api.domain.coffeechat.dto.type.YesOrNo;
import org.devridge.api.domain.coffeechat.entity.ChatMessage;
import org.devridge.api.domain.coffeechat.entity.ChatRoom;
import org.devridge.api.domain.coffeechat.entity.CoffeeChatRequest;
import org.devridge.api.infrastructure.coffeechat.ChatMessageRepository;
import org.devridge.api.infrastructure.coffeechat.ChatRoomRepository;
import org.devridge.api.infrastructure.coffeechat.CoffeeChatQuerydslRepository;
import org.devridge.api.infrastructure.coffeechat.CoffeeChatRequestRepository;
import org.devridge.api.domain.member.entity.Member;
import org.devridge.api.infrastructure.member.MemberRepository;
import org.devridge.api.common.exception.common.BadRequestException;
import org.devridge.api.common.exception.common.DataNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.devridge.api.common.util.SecurityContextHolderUtil.getMemberId;

@RequiredArgsConstructor
@Service
public class CoffeeChatService {

    // TODO: 내 요청 외에는 볼 수 없도록 인터셉터 설정

    private final ChatRoomRepository chatRoomRepository;
    private final MemberRepository memberRepository;
    private final CoffeeChatQuerydslRepository coffeeChatQuerydslRepository;
    private final CoffeeChatMapper coffeeChatMapper;
    private final ChatMessageRepository chatMessageRepository;
    private final CoffeeChatRequestRepository coffeeChatRequestRepository;

    @Transactional(readOnly = true)
    public List<GetAllMyChatRoom> getAllMyChatRoom(Long lastIndex) {
        Member member = this.getMember(getMemberId());

        if (lastIndex == null) {
            Long maxId = chatRoomRepository.findMaxId(member).orElse(0L);
            List<ChatRoom> chatRooms = coffeeChatQuerydslRepository.findAllMyChatRoomByMemberId(maxId, member);
            List<ChatMessage> chatMessages = this.getLastChatMessages(chatRooms);
            return coffeeChatMapper.toGetAllMyChatRooms(chatRooms, chatMessages, member);
        }

        List<ChatRoom> chatRooms = coffeeChatQuerydslRepository.findAllMyChatRoomByMemberId(lastIndex, member);
        List<ChatMessage> chatMessages = this.getLastChatMessages(chatRooms);
        return coffeeChatMapper.toGetAllMyChatRooms(chatRooms, chatMessages, member);
    }

    private List<ChatMessage> getLastChatMessages(List<ChatRoom> chatRooms) {
        List<ChatMessage> chatMessages = new ArrayList<>();

        // TODO: 반복계 쿼리 리팩토링
        for (ChatRoom room : chatRooms) {
            List<ChatMessage> message = coffeeChatQuerydslRepository.findLastChatMessageByChatRoomId(room);
            if (message.size() > 0) chatMessages.add(message.get(0));
        }

        return chatMessages;
    }

    @Transactional(readOnly = true)
    public List<GetAllChatMessage> getAllChatMessage(Long chatRoomId, Long lastIndex) {
        ChatRoom chatRoom = chatRoomRepository
                .findById(chatRoomId)
                .orElseThrow(DataNotFoundException::new);

        if (lastIndex == null) {
            Long maxId = chatMessageRepository.findMaxId(chatRoom).orElse(0L);
            List<ChatMessage> messages = coffeeChatQuerydslRepository
                    .findAllChatMessagesByChatRoomId(maxId, chatRoom);
            return coffeeChatMapper.toGetAllChatMessage(messages);
        }

        List<ChatMessage> messages = coffeeChatQuerydslRepository
                .findAllChatMessagesByChatRoomId(lastIndex, chatRoom);
        return coffeeChatMapper.toGetAllChatMessage(messages);
    }

    public Long createCoffeeChatRequest(CreateCoffeeChatRequest request) {
        // TODO: 비동기 처리
        Member fromMember = this.getMember(getMemberId());
        Member toMember = this.getMember(request.getToMemberId());
        CoffeeChatRequest coffeeChatRequest = coffeeChatMapper
                .toCoffeeChatRequest(toMember, fromMember, request.getMessage());

        return coffeeChatRequestRepository.save(coffeeChatRequest).getId();
    }

    @Transactional
    public CoffeeChatResult acceptOrRejectCoffeeChatRequest(
        Long requestId, AcceptOrRejectCoffeeChatRequest request
    ) {
        CoffeeChatRequest coffeeChatRequest = coffeeChatRequestRepository
            .findById(requestId)
            .orElseThrow(DataNotFoundException::new);

        switch (YesOrNo.valueOf(request.getAnswer())) {
            case Y:
                coffeeChatRequest.updateSuccess(true);
                coffeeChatRequestRepository.save(coffeeChatRequest);

                /* 승인 시 채팅방 생성 */
                createChatRoom(coffeeChatRequest);

                return new CoffeeChatResult("커피챗 요청이 승인되었습니다.");
            case N:
                coffeeChatRequest.updateSuccess(false);
                coffeeChatRequestRepository.save(coffeeChatRequest);

                return new CoffeeChatResult("커피챗 요청이 거절되었습니다.");
        }

        throw new BadRequestException();
    }

    private void createChatRoom(CoffeeChatRequest coffeeChatRequest) {
        Member firstMember = coffeeChatRequest.getFromMember();
        Member secondMember = coffeeChatRequest.getToMember();
        ChatRoom chatRoom = coffeeChatMapper.toChatRoom(firstMember, secondMember);
        chatRoomRepository.save(chatRoom);
    }

    public GetCoffeeChatRequestResponse getCoffeeChatRequest(Long requestId) {
        CoffeeChatRequest coffeeChatRequest = coffeeChatRequestRepository
            .findById(requestId)
            .orElseThrow(DataNotFoundException::new);

        coffeeChatRequest.updateReadAt();
        coffeeChatRequestRepository.save(coffeeChatRequest);

        return coffeeChatMapper.toGetCoffeeChatRequest(coffeeChatRequest);
    }

    public GetAllCoffeeChatRequest getCoffeeChatRequests(String viewOption, Long lastIndex) {
        Member member = this.getMember(getMemberId());

        if (lastIndex == null) {
            Long maxId = coffeeChatRequestRepository.findMaxId(member).orElse(0L);
            return this.getAllCoffeeChatRequest(viewOption, maxId, member);
        }

        return this.getAllCoffeeChatRequest(viewOption, lastIndex, member);
    }

    private GetAllCoffeeChatRequest getAllCoffeeChatRequest(String viewOption, Long lastIndex, Member member) {
        switch (ViewOption.valueOf(viewOption)) {
            case send:
                List<CoffeeChatRequest> sendResult = coffeeChatQuerydslRepository
                        .findAllSendCoffeeChatRequest(member, lastIndex);

                return coffeeChatMapper.toGetSendCoffeeChatRequests(sendResult);
            case receive:
                List<CoffeeChatRequest> receiveResult = coffeeChatQuerydslRepository
                        .findAllReceiveCoffeeChatRequest(member, lastIndex);

                return coffeeChatMapper.toGetReceiveCoffeeChatRequests(receiveResult);
        }

        throw new BadRequestException();
    }

    public GetAllChatMessage createChatMessage(CreateChatMessageRequest request, Long roomId, Long memberId) {
        Member member = this.getMember(memberId);
        ChatRoom chatRoom = this.getChatRoom(roomId);
        ChatMessage chatMessage = coffeeChatMapper.toChatMessage(request, member, chatRoom);

        ChatMessage saveResult = chatMessageRepository.save(chatMessage);

        return coffeeChatMapper.toGetChatMessage(saveResult);
    }

    public void deleteChatMessage(Long roomId, Long messageId) {
        ChatRoom chatRoom = this.getChatRoom(roomId);
        ChatMessage message = chatMessageRepository.findById(messageId).orElseThrow(DataNotFoundException::new);

        chatMessageRepository.deleteById(messageId);
    }

    private Member getMember(Long memberId) {
        // TODO: 채팅 테스트용 임시 아이디 생성, 추후 실제 Frontend와 연결 시 수정
        if (memberId == null) {
            memberId = 1L;
        }
        return memberRepository.findById(memberId).orElseThrow(DataNotFoundException::new);
    }

    private ChatRoom getChatRoom(Long roomId) {
        return chatRoomRepository.findById(roomId).orElseThrow(DataNotFoundException::new);
    }
}
