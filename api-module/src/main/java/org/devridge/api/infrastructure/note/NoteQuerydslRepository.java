package org.devridge.api.infrastructure.note;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.devridge.api.domain.member.entity.Member;
import org.devridge.api.domain.note.entity.NoteMessage;
import org.devridge.api.domain.note.entity.NoteRoom;
import org.devridge.api.domain.note.entity.QNoteMessage;
import org.devridge.api.domain.note.entity.QNoteRoom;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class NoteQuerydslRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private QNoteMessage noteMessage = QNoteMessage.noteMessage;
    private QNoteRoom noteRoom = QNoteRoom.noteRoom;

    public NoteMessage findLastNoteByNoteRoom(NoteRoom noteRoom) {
        return jpaQueryFactory
            .selectFrom(noteMessage)
            .where(noteMessage.noteRoom.eq(noteRoom))
            .orderBy(noteMessage.id.desc())
            .limit(1)
            .fetchOne();
    }

    public List<NoteRoom> findNoteRoomsByMember(Member member, Long lastId) {
        PathBuilder<NoteRoom> noteRoomPathBuilder = new PathBuilder<>(NoteRoom.class, "noteRoom");
        return jpaQueryFactory
            .selectFrom(noteRoom)
            .where(
                ltId(lastId, noteRoomPathBuilder),
                noteRoom.sender.eq(member)
                .or(noteRoom.receiver.eq(member))
            )
            .orderBy(noteRoom.lastNoteId.desc())
            .limit(10)
            .fetch();
    }

    private BooleanExpression ltId(Long Id, PathBuilder<?> pathBuilder) {
        if (Id == null) {
            return null;
        }
        return pathBuilder.getNumber("id", Long.class).lt(Id);
    }
}
