package org.devridge.api.domain.note.repository;

import org.devridge.api.domain.note.entity.ProjectParticipationNote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectParticipationNoteRepository extends JpaRepository<ProjectParticipationNote, Long> {
}
