package org.devridge.api.presentation.controller.note;

import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.devridge.api.application.note.NoteService;
import org.devridge.api.domain.note.dto.request.NoteRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class NoteController {

    private final NoteService noteService;
    @PostMapping("/api/notes")
    public ResponseEntity<Void> createNoteMessage(@RequestBody NoteRequest request) {
        Long noteRoomId = noteService.createNoteMessage(request);
        return ResponseEntity.created(URI.create("/api/notes/rooms/" + noteRoomId)).build();
    }
}
