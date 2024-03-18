package org.devridge.api.presentation.controller.note;

import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.devridge.api.application.note.ParticipationNoteService;
import org.devridge.api.domain.note.dto.request.ProjectParticipationNoteRequest;
import org.devridge.api.domain.note.dto.request.StudyParticipationNoteRequest;
import org.devridge.api.domain.note.dto.response.ReceivedParticipationNoteDetailResponse;
import org.devridge.api.domain.note.dto.response.ReceivedParticipationNoteListResponse;
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

    @GetMapping("/receive/project/{projectId}/participation/{participationNoteId}")
    public ResponseEntity<ReceivedParticipationNoteDetailResponse> getReceivedProjectParticipationNoteDetail(
        @PathVariable Long projectId,
        @PathVariable Long participationNoteId
    ) {
        ReceivedParticipationNoteDetailResponse receivedParticipationNoteDetailResponse =
                participationNoteService.getReceivedProjectParticipationNoteDetail(projectId, participationNoteId);
        return ResponseEntity.ok().body(receivedParticipationNoteDetailResponse);
    }

    @GetMapping("/project/receive")
    public ResponseEntity<Slice<ReceivedParticipationNoteListResponse>> getAllReceivedParticipationNote(
        @RequestParam(name = "lastId", required = false) Long lastId,
        Pageable pageable
    ) {
        Slice<ReceivedParticipationNoteListResponse> receivedParticipationNoteListResponses =
            participationNoteService.getAllReceivedParticipationNote(pageable, lastId);
        return ResponseEntity.ok().body(receivedParticipationNoteListResponses);
    }

    @DeleteMapping("/project/receive/{participationNoteId}")
    public ResponseEntity<Void> deleteParticipationNoteByReceiver(@PathVariable Long participationNoteId) {
        participationNoteService.deleteParticipationNoteByReceiver(participationNoteId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/project/send/{participationNoteId}")
    public ResponseEntity<Void> deleteParticipationNoteBySender(@PathVariable Long participationNoteId) {
        participationNoteService.deleteParticipationNoteBySender(participationNoteId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/project/send/{participationNoteId}")
    public ResponseEntity<SentParticipationNoteDetailResponse> getSentParticipationNoteDetail(
        @PathVariable Long participationNoteId
    ) {
        SentParticipationNoteDetailResponse sentParticipationNoteDetailResponse =
            participationNoteService.getSentParticipationNoteDetail(participationNoteId);
        return ResponseEntity.ok().body(sentParticipationNoteDetailResponse);
    }

    @GetMapping("/project/send")
    public ResponseEntity<Slice<SentParticipationNoteListResponse>> getAllSentParticipationNote(
        @RequestParam(name = "lastId", required = false) Long lastId,
        Pageable pageable
    ) {
        Slice<SentParticipationNoteListResponse> sentParticipationNoteListResponses =
            participationNoteService.getAllSentParticipationNote(pageable, lastId);
        return ResponseEntity.ok().body(sentParticipationNoteListResponses);
    }

    @PostMapping("/projects/{participationNoteId}")
    public ResponseEntity<Void> participationApproval(
        @PathVariable Long participationNoteId,
        @RequestParam Boolean approve
    ) {
        participationNoteService.participationApproval(participationNoteId, approve);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/studies/{studyId}")
    public ResponseEntity<Void> createStudyRequestNote(
        @PathVariable Long studyId,
        @RequestBody StudyParticipationNoteRequest participationNoteRequest
    ) {
        Long studyRequestNoteId =  participationNoteService.createStudyRequestNote(studyId, participationNoteRequest);
        return ResponseEntity.created(URI.create("/api/notes/participation/studies/" + studyRequestNoteId)).build();
    }

    @GetMapping("/studies/{participationNoteId}")
    public ResponseEntity<ReceivedParticipationNoteDetailResponse> getReceivedStudyParticipationNoteDetail(
        @PathVariable Long participationNoteId
    ) {
        ReceivedParticipationNoteDetailResponse receivedParticipationNoteDetailResponse =
            participationNoteService.getReceivedStudyParticipationNoteDetail(participationNoteId);
        return ResponseEntity.ok().body(receivedParticipationNoteDetailResponse);
    }

    @GetMapping("/studies/send/{participationNoteId}")
    public ResponseEntity<SentParticipationNoteDetailResponse> getSentStudyParticipationNoteDetail(
        @PathVariable Long participationNoteId
    ) {
        SentParticipationNoteDetailResponse sentParticipationNoteDetailResponse =
            participationNoteService.getSentStudyParticipationNoteDetail(participationNoteId);
        return ResponseEntity.ok().body(sentParticipationNoteDetailResponse);
    }

    @DeleteMapping("/studies/receive/{participationNoteId}")
    public ResponseEntity<Void> deleteStudyParticipationNoteByReceiver(@PathVariable Long participationNoteId) {
        participationNoteService.deleteStudyParticipationNoteByReceiver(participationNoteId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/studies/send/{participationNoteId}")
    public ResponseEntity<Void> deleteStudyParticipationNoteBySender(@PathVariable Long participationNoteId) {
        participationNoteService.deleteStudyParticipationNoteBySender(participationNoteId);
        return ResponseEntity.ok().build();
    }
}
