package org.devridge.api.presentation.controller.note;

import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.devridge.api.application.note.ParticipationNoteService;
import org.devridge.api.domain.note.dto.request.ProjectParticipationNoteRequest;
import org.devridge.api.domain.note.dto.request.StudyParticipationNoteRequest;
import org.devridge.api.domain.note.dto.response.ReceivedParticipationNoteDetailResponse;
import org.devridge.api.domain.note.dto.response.ReceivedParticipationNoteListAndCountUnreadNoteResponse;
import org.devridge.api.domain.note.dto.response.SentParticipationNoteDetailResponse;
import org.devridge.api.domain.note.dto.response.SentParticipationNoteListResponse;
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
@RequestMapping("/api/notes")
public class ParticipationNoteController {

    private final ParticipationNoteService participationNoteService;

    @PostMapping("/project/{projectId}/participation")
    public ResponseEntity<Void> createProjectParticipationNote(
        @PathVariable Long projectId,
        @RequestBody ProjectParticipationNoteRequest participationNoteRequest
    ) {
        Long projectRequestNote = participationNoteService.createProjectParticipationNote(projectId, participationNoteRequest);
        return ResponseEntity.created(URI.create("/api/notes/participation/project/" + projectRequestNote)).build();
    }

    @GetMapping("/receive/participation/{participationNoteId}")
    public ResponseEntity<ReceivedParticipationNoteDetailResponse> getReceivedParticipationNoteDetail(
        @PathVariable Long participationNoteId
    ) {
        ReceivedParticipationNoteDetailResponse receivedParticipationNoteDetailResponse =
                participationNoteService.getReceivedParticipationNoteDetail(participationNoteId);
        return ResponseEntity.ok().body(receivedParticipationNoteDetailResponse);
    }

    @GetMapping("/receive/participation")
    public ResponseEntity<ReceivedParticipationNoteListAndCountUnreadNoteResponse> getAllReceivedParticipationNote(
        @RequestParam(name = "lastId", required = false) Long lastId,
        Pageable pageable
    ) {
        ReceivedParticipationNoteListAndCountUnreadNoteResponse receivedParticipationNoteListResponses =
                participationNoteService.getAllReceivedParticipationNote(pageable, lastId);
        return ResponseEntity.ok().body(receivedParticipationNoteListResponses);
    }

    @DeleteMapping("/receive/participation/{participationNoteId}")
    public ResponseEntity<Void> deleteParticipationNoteByReceiver(@PathVariable Long participationNoteId) {
        participationNoteService.deleteParticipationNoteByReceiver(participationNoteId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/send/participation/{participationNoteId}")
    public ResponseEntity<Void> deleteParticipationNoteBySender(@PathVariable Long participationNoteId) {
        participationNoteService.deleteParticipationNoteBySender(participationNoteId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/send/participation/{participationNoteId}")
    public ResponseEntity<SentParticipationNoteDetailResponse> getSentParticipationNoteDetail(
        @PathVariable Long participationNoteId
    ) {
        SentParticipationNoteDetailResponse sentParticipationNoteDetailResponse =
                participationNoteService.getSentParticipationNoteDetail(participationNoteId);
        return ResponseEntity.ok().body(sentParticipationNoteDetailResponse);
    }

    @GetMapping("/send/participation")
    public ResponseEntity<Slice<SentParticipationNoteListResponse>> getAllSentParticipationNote(
        @RequestParam(name = "lastId", required = false) Long lastId,
        Pageable pageable
    ) {
        Slice<SentParticipationNoteListResponse> sentParticipationNoteListResponses =
                participationNoteService.getAllSentParticipationNote(pageable, lastId);
        return ResponseEntity.ok().body(sentParticipationNoteListResponses);
    }

    @PostMapping("/participation/{participationNoteId}")
    public ResponseEntity<Void> participationApproval(
        @PathVariable Long participationNoteId,
        @RequestParam Boolean approve
    ) {
        participationNoteService.participationApproval(participationNoteId, approve);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/studies/{studyId}/participation")
    public ResponseEntity<Void> createStudyParticipationNote(
        @PathVariable Long studyId,
        @RequestBody StudyParticipationNoteRequest participationNoteRequest
    ) {
        Long studyRequestNoteId =  participationNoteService.createStudyParticipationNote(studyId, participationNoteRequest);
        return ResponseEntity.created(URI.create("/api/notes/participation/studies/" + studyRequestNoteId)).build();
    }
}
