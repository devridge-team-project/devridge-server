package org.devridge.api.domain.note.repository;

import java.util.List;
import org.devridge.api.domain.member.entity.Member;
import org.devridge.api.domain.note.entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoteRepository extends JpaRepository<Note, Long> {

    List<Note> findAllByReceiver(Member member);
    List<Note> findAllBySender(Member member);
}
