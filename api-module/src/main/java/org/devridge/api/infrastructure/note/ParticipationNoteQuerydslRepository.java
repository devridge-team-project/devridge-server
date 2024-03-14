package org.devridge.api.infrastructure.note;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.devridge.api.domain.note.entity.ProjectParticipationNote;
import org.devridge.api.domain.note.entity.QProjectParticipationNote;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class ParticipationNoteQuerydslRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private QProjectParticipationNote projectParticipationNote = QProjectParticipationNote.projectParticipationNote;

    public List<ProjectParticipationNote> searchByProjectParticipationNote(Long lastId, Long id, Pageable pageable) {
        return jpaQueryFactory
            .selectFrom(projectParticipationNote)
            .where(projectParticipationNote.receiver.id.eq(id),
                ltId(lastId),
                projectParticipationNote.isDeleted.eq(false)
            )
            .limit(pageable.getPageSize() + 1)
            .fetch();
    }

    public List<ProjectParticipationNote> findSenderSortByProjectParticipationNote(Long lastId, Long senderId, Pageable pageable) {
        return jpaQueryFactory
            .selectFrom(projectParticipationNote)
            .where(projectParticipationNote.sender.id.eq(senderId),
                ltId(lastId),
                projectParticipationNote.isDeleted.eq(false)
            )
            .limit(pageable.getPageSize() + 1)
            .fetch();
    }

    private BooleanExpression ltId(Long projectParticipationNoteId) {
        if (projectParticipationNoteId == null) {
            return null;
        }

        return projectParticipationNote.id.lt(projectParticipationNoteId);
    }
}
