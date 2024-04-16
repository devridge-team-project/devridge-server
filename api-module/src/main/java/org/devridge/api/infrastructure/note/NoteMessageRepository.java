package org.devridge.api.infrastructure.note;

import org.devridge.api.domain.note.entity.NoteMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoteMessageRepository extends JpaRepository<NoteMessage, Long> {

}
