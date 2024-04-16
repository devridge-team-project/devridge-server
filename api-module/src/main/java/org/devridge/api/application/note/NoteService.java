package org.devridge.api.application.note;


import lombok.RequiredArgsConstructor;
import org.devridge.api.common.exception.common.DataNotFoundException;
import org.devridge.api.common.util.SecurityContextHolderUtil;
import org.devridge.api.domain.member.entity.Member;
import org.devridge.api.domain.note.dto.request.NoteRequest;
import org.devridge.api.domain.note.entity.NoteMessage;
import org.devridge.api.domain.note.entity.NoteRoom;
import org.devridge.api.infrastructure.member.MemberRepository;
import org.devridge.api.infrastructure.note.NoteMessageRepository;
import org.devridge.api.infrastructure.note.NoteRoomRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NoteService {

    private final NoteMessageRepository noteMessageRepository;
    private final MemberRepository memberRepository;
    private final NoteRoomRepository noteRoomRepository;
    private final NoteMapper noteMapper;

    @Transactional
    public Long createNoteMessage(NoteRequest request) {
        Member accessMember = SecurityContextHolderUtil.getMember();
        Member receiveMember = getMember(request.getReceiverId());

        NoteRoom noteRoom = noteRoomRepository.findNoteRoomByMemberIds(accessMember, receiveMember)
                .orElseGet(() -> createNoteRoom(accessMember, receiveMember));
        NoteMessage message = noteMapper.toNoteMessage(noteRoom, request.getContent(), accessMember);
        Long lastNoteId = noteMessageRepository.save(message).getId();
        noteRoom.updateLastNoteId(lastNoteId);
        return noteRoom.getId();
    }

    private NoteRoom createNoteRoom(Member accessMember, Member receiverMember) {
        NoteRoom newNoteRoom = new NoteRoom(accessMember, receiverMember);
        return noteRoomRepository.save(newNoteRoom);
    }

    private Member getMember(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(DataNotFoundException::new);
    }
}
