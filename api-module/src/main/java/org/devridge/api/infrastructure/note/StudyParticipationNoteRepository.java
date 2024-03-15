package org.devridge.api.infrastructure.note;

import org.devridge.api.domain.note.entity.StudyParticipationNote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudyParticipationNoteRepository extends JpaRepository<StudyParticipationNote, Long> {

}
