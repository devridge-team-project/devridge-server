package org.devridge.api.application.note;

import org.devridge.api.domain.member.entity.Member;
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
}
