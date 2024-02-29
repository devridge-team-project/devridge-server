package org.devridge.api.domain.coffeechat.service;

import lombok.RequiredArgsConstructor;

import org.devridge.api.domain.coffeechat.dto.request.AcceptOrRejectCoffeeChatRequest;
import org.devridge.api.domain.coffeechat.dto.request.CreateCoffeeChatRequest;
import org.devridge.api.domain.coffeechat.dto.response.CoffeeChatResult;
import org.devridge.api.domain.coffeechat.dto.response.GetAllMyChatRoom;
import org.devridge.api.domain.coffeechat.dto.response.GetAllChatMessage;
import org.devridge.api.domain.coffeechat.dto.response.GetCoffeeChatRequest;
import org.devridge.api.domain.coffeechat.dto.type.YesOrNo;
import org.devridge.api.domain.coffeechat.entity.ChatMessage;
import org.devridge.api.domain.coffeechat.entity.ChatRoom;
import org.devridge.api.domain.coffeechat.entity.CoffeeChatRequest;
import org.devridge.api.domain.coffeechat.mapper.CoffeeChatMapper;
import org.devridge.api.domain.coffeechat.repository.ChatMessageRepository;
import org.devridge.api.domain.coffeechat.repository.ChatRoomRepository;
import org.devridge.api.domain.coffeechat.repository.CoffeeChatQuerydslRepository;
import org.devridge.api.domain.coffeechat.repository.CoffeeChatRequestRepository;
import org.devridge.api.domain.member.entity.Member;
import org.devridge.api.domain.member.repository.MemberRepository;
import org.devridge.api.exception.common.BadRequestException;
import org.devridge.api.exception.common.DataNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.devridge.api.util.SecurityContextHolderUtil.getMemberId;

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
            return coffeeChatMapper.toGetAllMyChatRooms(chatRooms, member);
        }

        List<ChatRoom> chatRooms = coffeeChatQuerydslRepository.findAllMyChatRoomByMemberId(lastIndex, member);
        return coffeeChatMapper.toGetAllMyChatRooms(chatRooms, member);
    }

    @Transactional(readOnly = true)
    public List<GetAllChatMessage> getAllChatMessage(Long chatRoomId, Long lastIndex) {
        // TODO: 내 채팅방이 아닌 경우 접근 금지 인터셉터 추가
        ChatRoom chatRoom = chatRoomRepository
                .findById(chatRoomId)
                .orElseThrow(() -> new DataNotFoundException());

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
                Member firstMember = coffeeChatRequest.getFromMember();
                Member secondMember = coffeeChatRequest.getToMember();
                ChatRoom chatRoom = coffeeChatMapper.toChatRoom(firstMember, secondMember);
                chatRoomRepository.save(chatRoom);

                return new CoffeeChatResult("커피챗 요청이 승인되었습니다.");
            case N:
                coffeeChatRequest.updateSuccess(false);
                coffeeChatRequestRepository.save(coffeeChatRequest);

                return new CoffeeChatResult("커피챗 요청이 거절되었습니다.");
        }

        throw new BadRequestException();
    }

    public GetCoffeeChatRequest getCoffeeChatRequest(Long requestId) {
        CoffeeChatRequest coffeeChatRequest = coffeeChatRequestRepository
            .findById(requestId)
            .orElseThrow(DataNotFoundException::new);

        coffeeChatRequest.updateReadAt();
        coffeeChatRequestRepository.save(coffeeChatRequest);

        return coffeeChatMapper.toGetCoffeeChatRequest(coffeeChatRequest);
    }

    private Member getMember(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(DataNotFoundException::new);
    }
}
