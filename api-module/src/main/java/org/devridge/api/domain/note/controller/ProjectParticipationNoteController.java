package org.devridge.api.domain.note.controller;

import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.devridge.api.domain.note.dto.request.ProjectParticipationNoteRequest;
import org.devridge.api.domain.note.service.ProjectParticipationNoteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notes/participation")
public class ProjectParticipationNoteController {

    private final ProjectParticipationNoteService projectParticipationNoteService;

    @PostMapping("/project/{projectId}")
    public ResponseEntity<Void> createRequestNote(
        @PathVariable Long projectId,
        @RequestBody ProjectParticipationNoteRequest participationNoteRequest
    ) {
        Long projectRequestNote = projectParticipationNoteService.createRequestNote(projectId, participationNoteRequest);
        return ResponseEntity.created(URI.create("/api/notes/participation/project/" + projectRequestNote)).build();
    }
}
