package org.devridge.api.domain.note.controller;

import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.devridge.api.domain.note.dto.request.NoteRequest;
import org.devridge.api.domain.note.dto.response.NoteResponse;
import org.devridge.api.domain.note.service.NoteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/notes")
@RestController
@RequiredArgsConstructor
public class NoteController {

    private final NoteService noteService;

    @PostMapping
    public ResponseEntity<Void> sendNote(@RequestBody NoteRequest noteRequest) {
        Long noteId = noteService.createNote(noteRequest);
        return ResponseEntity.created(URI.create("/api/notes/" + noteId)).build();
    }

    @GetMapping("/receive")
    public ResponseEntity<List<NoteResponse>> getReceivedNotes() {
        List<NoteResponse> noteResponses = noteService.getReceivedNotes();
        return ResponseEntity.ok().body(noteResponses);
    }

    @DeleteMapping("/receive/{noteId}")
    public ResponseEntity<Void> deleteNoteByReceiver(@PathVariable Long noteId) {
        noteService.deleteNoteByReceiver(noteId);
        return ResponseEntity.ok().build();
    }

}
