package org.devridge.api.presentation.controller.community.study;

import java.net.URI;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.devridge.api.application.community.study.StudyCommentService;
import org.devridge.api.domain.community.dto.request.StudyCommentRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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

    @PutMapping("/{commentId}")
    public ResponseEntity<Void> updateComment(
        @RequestBody StudyCommentRequest commentRequest,
        @PathVariable Long commentId,
        @PathVariable Long studyId
    ) {
        studyCommentService.updateComment(studyId, commentId, commentRequest);
        return ResponseEntity.ok().build();
    }
}