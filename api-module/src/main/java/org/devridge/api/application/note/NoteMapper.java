package org.devridge.api.application.note;

import static org.devridge.api.common.util.MemberUtil.toMember;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.devridge.api.domain.member.entity.Member;
import org.devridge.api.domain.note.dto.response.GetAllNote;
import org.devridge.api.domain.note.dto.response.GetAllRoom;
import org.devridge.api.domain.note.entity.NoteMessage;
import org.devridge.api.domain.note.entity.NoteRoom;
import org.springframework.stereotype.Component;

@Component
public class NoteMapper {

    public NoteMessage toNoteMessage(NoteRoom noteRoom, String content, Member sender) {
        return NoteMessage.builder()
                .noteRoom(noteRoom)
                .content(content)
                .sender(sender)
                .build();
    }

    public List<GetAllRoom> toGetAllRooms(Map<Member, NoteMessage> otherMembersAndNoteMessages) {
        List<GetAllRoom> getAllRooms = new ArrayList<>();

        for (Map.Entry<Member, NoteMessage> entry : otherMembersAndNoteMessages.entrySet()) {
            Member otherMember = entry.getKey();
            NoteMessage noteMessage = entry.getValue();
            getAllRooms.add(
                toGetAllRoom(otherMember, noteMessage)
            );
        }

        return getAllRooms;
    }

    public GetAllRoom toGetAllRoom(Member member, NoteMessage noteMessage) {
        return GetAllRoom.builder()
            .id(noteMessage.getNoteRoom().getId())
            .userInformation(toMember(member))
            .content(noteMessage.getContent())
            .createdAt(noteMessage.getCreatedAt())
            .build();
    }

    public List<GetAllNote> toGetAllNotes(List<NoteMessage> noteMessages) {
        List<GetAllNote> getAllNotes = new ArrayList<>();

        for (NoteMessage noteMessage : noteMessages) {
            getAllNotes.add(
                toGetAllNote(noteMessage)
            );
        }

        return getAllNotes;
    }

    public GetAllNote toGetAllNote(NoteMessage noteMessage) {
        return GetAllNote.builder()
            .id(noteMessage.getId())
            .senderId(noteMessage.getSender().getId())
            .content(noteMessage.getContent())
            .createdAt(noteMessage.getCreatedAt())
            .build();
    }
}
