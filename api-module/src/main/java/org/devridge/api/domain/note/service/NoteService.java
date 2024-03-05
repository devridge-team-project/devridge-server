package org.devridge.api.domain.note.service;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.devridge.api.domain.member.entity.Member;
import org.devridge.api.domain.member.repository.MemberRepository;
import org.devridge.api.domain.note.dto.request.NoteRequest;
import org.devridge.api.domain.note.dto.response.NoteResponse;
import org.devridge.api.domain.note.entity.Note;
import org.devridge.api.domain.note.repository.NoteRepository;
import org.devridge.api.util.SecurityContextHolderUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public List<NoteResponse> getReceivedNotes() {
        Member receiver = SecurityContextHolderUtil.getMember();
        List<Note> notes = noteRepository.findAllByReceiver(receiver);

        List<NoteResponse> noteResponses = new ArrayList<>();

        for (Note note : notes) {
            if (!note.getDeletedByReceiver()) {
                noteResponses.add(
                    NoteResponse.builder()
                        .senderName(note.getSender().getNickname())
                        .title(note.getTitle())
                        .content(note.getContent())
                        .receiveTime(note.getSendAt())
                        .build()
                );
            }
        }
        return noteResponses;
    }

    @Transactional
    public void deleteNoteByReceiver(Long NoteId) {
        Member receiver = SecurityContextHolderUtil.getMember();
        Note note = noteRepository.findById(NoteId).orElseThrow();

        if (receiver.getId().equals(note.getReceiver().getId())) {
            note.deleteByReceiver();
            if (note.isDeleted()) {
                noteRepository.delete(note);
            }
        }
    }
}
