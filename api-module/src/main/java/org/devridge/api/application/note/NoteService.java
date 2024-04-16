package org.devridge.api.application.note;


import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.devridge.api.common.exception.common.DataNotFoundException;
import org.devridge.api.common.util.SecurityContextHolderUtil;
import org.devridge.api.domain.member.entity.Member;
import org.devridge.api.domain.note.dto.request.NoteRequest;
import org.devridge.api.domain.note.dto.response.GetAllNote;
import org.devridge.api.domain.note.dto.response.GetAllRoom;
import org.devridge.api.domain.note.entity.NoteMessage;
import org.devridge.api.domain.note.entity.NoteRoom;
import org.devridge.api.domain.note.exception.NoteForbiddenException;
import org.devridge.api.infrastructure.member.MemberRepository;
import org.devridge.api.infrastructure.note.NoteMessageRepository;
import org.devridge.api.infrastructure.note.NoteQuerydslRepository;
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
    private final NoteQuerydslRepository noteQuerydslRepository;

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

    public List<GetAllRoom> getAllNoteRoom(Long lastId) {
        Member accessMember = SecurityContextHolderUtil.getMember();
        List<NoteRoom> noteRooms = noteQuerydslRepository.findNoteRoomsByMember(accessMember, lastId);
        Map<Member, NoteMessage> otherMembersAndNoteMessages = new LinkedHashMap<>();

        for (NoteRoom noteRoom : noteRooms) {
            Member otherMember;
            if (accessMember.getId().equals(noteRoom.getSender().getId())) {
                otherMember = noteRoom.getReceiver();
            } else {
                otherMember = noteRoom.getSender();
            }

            NoteMessage noteMessage = noteQuerydslRepository.findLastNoteByNoteRoom(noteRoom);
            otherMembersAndNoteMessages.put(otherMember, noteMessage);
        }

        return noteMapper.toGetAllRooms(otherMembersAndNoteMessages);
    }

    public List<GetAllNote> getAllNote(Long roomId, Long lastId) {
        Long accessMemberId = SecurityContextHolderUtil.getMemberId();
        NoteRoom noteRoom = getNoteRoom(roomId);

        if (!noteRoom.getSender().getId().equals(accessMemberId) && !noteRoom.getReceiver().getId().equals(accessMemberId)) {
            throw new NoteForbiddenException(403, "회원님이 보내거나 받은 쪽지 목록이 아닙니다.");
        }
        List<NoteMessage> noteMessages = noteQuerydslRepository.findNotesByRoomId(roomId, lastId);
        return noteMapper.toGetAllNotes(noteMessages);
    }

    private Member getMember(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(DataNotFoundException::new);
    }

    private NoteRoom getNoteRoom(Long roomId) {
        return noteRoomRepository.findById(roomId).orElseThrow(DataNotFoundException::new);
    }
}
