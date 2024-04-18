package org.devridge.api.domain.note.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devridge.api.common.entity.BaseEntity;
import org.devridge.api.domain.member.entity.Member;
import org.hibernate.annotations.DynamicInsert;

@Entity
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class NoteMessage extends BaseEntity {

    @NotNull
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    private Member sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "note_room_id")
    private NoteRoom noteRoom;

    @Builder
    public NoteMessage(String content, Member sender, NoteRoom noteRoom) {
        this.content = content;
        this.sender = sender;
        this.noteRoom = noteRoom;
    }
}
