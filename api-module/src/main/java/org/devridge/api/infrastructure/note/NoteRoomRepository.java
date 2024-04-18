package org.devridge.api.infrastructure.note;

import java.util.Optional;
import org.devridge.api.domain.member.entity.Member;
import org.devridge.api.domain.note.entity.NoteRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NoteRoomRepository extends JpaRepository<NoteRoom, Long> {

    @Query(
        value = "SELECT nr " +
                "FROM NoteRoom nr " +
                "WHERE (nr.sender = :sender AND nr.receiver = :receiver) " +
                "OR (nr.sender = :receiver AND nr.receiver = :sender)"
    )
    Optional<NoteRoom> findNoteRoomByMemberIds(@Param("sender") Member sender, @Param("receiver") Member receiver);
}
