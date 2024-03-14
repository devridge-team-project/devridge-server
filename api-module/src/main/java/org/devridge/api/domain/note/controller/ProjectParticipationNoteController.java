package org.devridge.api.domain.note.controller;

import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.devridge.api.domain.note.dto.request.ProjectParticipationNoteRequest;
import org.devridge.api.domain.note.dto.response.ReceivedParticipationNoteDetailResponse;
import org.devridge.api.domain.note.dto.response.ReceivedParticipationNoteListResponse;
import org.devridge.api.domain.note.dto.response.SentParticipationNoteDetailResponse;
import org.devridge.api.domain.note.dto.response.SentParticipationNoteListResponse;
import org.devridge.api.domain.note.service.ProjectParticipationNoteService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @GetMapping("/project/{participationNoteId}")
    public ResponseEntity<ReceivedParticipationNoteDetailResponse> getReceivedParticipationNoteDetail(
        @PathVariable Long participationNoteId
    ) {
        ReceivedParticipationNoteDetailResponse receivedParticipationNoteDetailResponse =
                projectParticipationNoteService.getReceivedParticipationNoteDetail(participationNoteId);
        return ResponseEntity.ok().body(receivedParticipationNoteDetailResponse);
    }

    @GetMapping("/project/receive")
    public ResponseEntity<Slice<ReceivedParticipationNoteListResponse>> getAllReceivedParticipationNote(
        @RequestParam(name = "lastId", required = false) Long lastId,
        Pageable pageable
    ) {
        Slice<ReceivedParticipationNoteListResponse> receivedParticipationNoteListResponses =
                projectParticipationNoteService.getAllReceivedParticipationNote(pageable, lastId);
        return ResponseEntity.ok().body(receivedParticipationNoteListResponses);
    }

    @DeleteMapping("/project/receive/{participationNoteId}")
    public ResponseEntity<Void> deleteParticipationNoteByReceiver(@PathVariable Long participationNoteId) {
        projectParticipationNoteService.deleteParticipationNoteByReceiver(participationNoteId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/project/send/{participationNoteId}")
    public ResponseEntity<Void> deleteParticipationNoteBySender(@PathVariable Long participationNoteId) {
        projectParticipationNoteService.deleteParticipationNoteBySender(participationNoteId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/project/send/{participationNoteId}")
    public ResponseEntity<SentParticipationNoteDetailResponse> getSentParticipationNoteDetail(
        @PathVariable Long participationNoteId
    ) {
        SentParticipationNoteDetailResponse sentParticipationNoteDetailResponse =
            projectParticipationNoteService.getSentParticipationNoteDetail(participationNoteId);
        return ResponseEntity.ok().body(sentParticipationNoteDetailResponse);
    }

    @GetMapping("/project/send")
    public ResponseEntity<Slice<SentParticipationNoteListResponse>> getAllSentParticipationNote(
        @RequestParam(name = "lastId", required = false) Long lastId,
        Pageable pageable
    ) {
        Slice<SentParticipationNoteListResponse> sentParticipationNoteListResponses =
            projectParticipationNoteService.getAllSentParticipationNote(pageable, lastId);
        return ResponseEntity.ok().body(sentParticipationNoteListResponses);
    }

    @PostMapping("/projects/{participationNoteId}")
    public ResponseEntity<Void> participationApproval(
        @PathVariable Long participationNoteId,
        @RequestParam Boolean approve
    ) {
        projectParticipationNoteService.participationApproval(participationNoteId, approve);
        return ResponseEntity.ok().build();
    }
}
