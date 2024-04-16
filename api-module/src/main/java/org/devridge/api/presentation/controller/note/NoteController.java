package org.devridge.api.presentation.controller.note;

import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.devridge.api.application.note.NoteService;
import org.devridge.api.domain.note.dto.request.NoteRequest;
import org.devridge.api.domain.note.dto.response.GetAllNote;
import org.devridge.api.domain.note.dto.response.GetAllRoom;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
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

    @GetMapping("/api/notes/rooms")
    public ResponseEntity<List<GetAllRoom>> getAllNoteRoom(@RequestParam(required = false) Long lastId) {
        List<GetAllRoom> notes = noteService.getAllNoteRoom(lastId);
        return ResponseEntity.ok().body(notes);
    }

    @GetMapping("/api/notes/rooms/{roomId}")
    public ResponseEntity<List<GetAllNote>> getAllNote(
        @PathVariable Long roomId,
        @RequestParam(required = false) Long lastId
    ) {
        List<GetAllNote> getAllNotes = noteService.getAllNote(roomId, lastId);
        return ResponseEntity.ok().body(getAllNotes);
    }
}