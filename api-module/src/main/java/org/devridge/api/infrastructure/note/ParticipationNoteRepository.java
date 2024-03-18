package org.devridge.api.infrastructure.note;

import org.devridge.api.domain.note.entity.ParticipationNote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParticipationNoteRepository extends JpaRepository<ParticipationNote, Long> {

}
