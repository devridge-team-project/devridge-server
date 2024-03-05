package org.devridge.api.domain.note.service;

import lombok.RequiredArgsConstructor;
import org.devridge.api.domain.member.entity.Member;
import org.devridge.api.domain.member.repository.MemberRepository;
import org.devridge.api.domain.note.dto.request.NoteRequest;
import org.devridge.api.domain.note.entity.Note;
import org.devridge.api.domain.note.repository.NoteRepository;
import org.devridge.api.util.SecurityContextHolderUtil;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NoteService {

    private final NoteRepository noteRepository;
    private final MemberRepository memberRepository;

    public Long createNote(NoteRequest noteRequest) {
        Member sender = SecurityContextHolderUtil.getMember();
        Member receiver = memberRepository.findByNickname(noteRequest.getReceiverName()).orElseThrow();

        Note note = Note.builder()
            .receiver(receiver)
            .sender(sender)
            .title(noteRequest.getTitle())
            .content(noteRequest.getContent())
            .build();

        return noteRepository.save(note).getId();
    }
}
