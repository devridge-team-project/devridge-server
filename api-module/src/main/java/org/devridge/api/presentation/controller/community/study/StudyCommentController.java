package org.devridge.api.presentation.controller.community.study;

import java.net.URI;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.devridge.api.application.community.study.StudyCommentService;
import org.devridge.api.domain.community.dto.request.StudyCommentRequest;
import org.devridge.api.domain.community.dto.response.StudyCommentResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/community/studies/{studyId}/comments")
@RestController
public class StudyCommentController {

    private final StudyCommentService studyCommentService;

    @PostMapping
    public ResponseEntity<Void> createStudyComment(
        @Valid @RequestBody StudyCommentRequest commentRequest,
        @PathVariable Long studyId
    ) {
        Long commentId = studyCommentService.createStudyComment(studyId, commentRequest);
        return ResponseEntity.created(URI.create("/api/community/studies/" + studyId + "/comments/" + commentId)).build();
    }

    @GetMapping
    public ResponseEntity<Slice<StudyCommentResponse>> getAllComments(
        @PathVariable Long studyId,
        @RequestParam(name = "lastId", required = false) Long lastId,
        Pageable pageable
    ) {
        Slice<StudyCommentResponse> commentResponses = studyCommentService.getAllStudyComment(studyId, lastId, pageable);
        return ResponseEntity.ok().body(commentResponses);
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<Void> updateComment(
        @RequestBody StudyCommentRequest commentRequest,
        @PathVariable Long commentId,
        @PathVariable Long studyId
    ) {
        studyCommentService.updateComment(studyId, commentId, commentRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(
        @PathVariable Long studyId,
        @PathVariable Long commentId
    ) {
        studyCommentService.deleteComment(studyId, commentId);
        return ResponseEntity.ok().build();
    }
}