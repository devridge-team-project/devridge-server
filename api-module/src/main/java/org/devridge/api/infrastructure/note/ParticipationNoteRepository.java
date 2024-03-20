package org.devridge.api.infrastructure.note;

import java.util.Optional;
import org.devridge.api.domain.note.entity.ParticipationNote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ParticipationNoteRepository extends JpaRepository<ParticipationNote, Long> {

    @Query(
        value = "SELECT p " +
                "FROM ParticipationNote p " +
                "WHERE p.id = :id AND p.isDeleted = false AND p.deletedBySender = false"
    )
    Optional<ParticipationNote> findSenderParticipationNoteById(@Param("id") Long id);

    @Query(
        value = "SELECT p " +
                "FROM ParticipationNote p " +
                "WHERE p.id = :id AND p.isDeleted = false AND p.deletedByReceiver = false"
    )
    Optional<ParticipationNote> findReceiverParticipationNoteById(@Param("id") Long id);
}
