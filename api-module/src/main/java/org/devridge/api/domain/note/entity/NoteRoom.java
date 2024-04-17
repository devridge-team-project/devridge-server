package org.devridge.api.domain.note.entity;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devridge.api.common.entity.BaseEntity;
import org.devridge.api.domain.member.entity.Member;
import org.hibernate.annotations.DynamicInsert;

@Entity
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class NoteRoom extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    private Member sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id")
    private Member receiver;

    private Long lastNoteId;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "noteRoom")
    private List<NoteMessage> noteMessages = new ArrayList<>();

    public NoteRoom(Member sender, Member receiver) {
        this.sender = sender;
        this.receiver = receiver;
    }

    public void updateLastNoteId(Long lastNoteId) {
        this.lastNoteId = lastNoteId;
    }
}
