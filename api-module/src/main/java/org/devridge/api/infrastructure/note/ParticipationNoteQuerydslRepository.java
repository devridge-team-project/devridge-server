package org.devridge.api.infrastructure.note;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.devridge.api.common.dto.UserInformation;
import org.devridge.api.domain.note.dto.response.ReceivedParticipationNoteListResponse;
import org.devridge.api.domain.note.dto.response.SentParticipationNoteListResponse;
import org.devridge.api.domain.note.entity.QParticipationNote;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class ParticipationNoteQuerydslRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private QParticipationNote participationNote = QParticipationNote.participationNote;

    public List<ReceivedParticipationNoteListResponse> findAllReceivedParticipationNoteListResponses(Long lastId,
            Long receiverId, Pageable pageable) {
        return jpaQueryFactory
            .select(Projections.fields(ReceivedParticipationNoteListResponse.class,
                participationNote.id.as("participationId"),
                Projections.fields(UserInformation.class,
                    participationNote.sender.id,
                    participationNote.sender.nickname,
                    participationNote.sender.profileImageUrl,
                    participationNote.sender.introduction
                ).as("sendMember"),
                participationNote.createdAt.as("receivedTime"),
                participationNote.isApproved
            ))
            .from(
                participationNote
            )
            .where(
                ltId(lastId),
                participationNote.receiver.id.eq(receiverId),
                participationNote.isDeleted.eq(false),
                participationNote.deletedByReceiver.eq(false)
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

    private BooleanExpression ltId(Long participationNoteId) {
        if (participationNoteId == null) {
            return null;
        }

        return participationNote.id.lt(participationNoteId);
    }
}
