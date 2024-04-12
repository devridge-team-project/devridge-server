package org.devridge.api.presentation.controller.community.project;

import java.net.URI;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.devridge.api.application.community.project.ProjectCommentService;
import org.devridge.api.domain.community.dto.request.ProjectCommentRequest;
import org.devridge.api.domain.community.dto.response.ProjectCommentResponse;
import org.springframework.data.domain.Pageable;
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
@RequestMapping("/api/community/projects/{projectId}/comments")
@RestController
public class ProjectCommentController {

    private final ProjectCommentService projectCommentService;

    @PostMapping
    public ResponseEntity<Void> createProjectComment(
        @Valid @RequestBody ProjectCommentRequest commentRequest,
        @PathVariable Long projectId
    ) {
        Long commentId = projectCommentService.createProjectComment(projectId, commentRequest);
        return ResponseEntity.created(URI.create("/api/community/projects/" + projectId + "/comments/" + commentId)).build();
    }

    @GetMapping
    public ResponseEntity<List<ProjectCommentResponse>> getAllComments(
        @PathVariable Long projectId,
        @RequestParam(name = "lastId", required = false) Long lastId,
        Pageable pageable
    ) {
        List<ProjectCommentResponse> commentResponses = projectCommentService.getAllProjectComment(projectId, lastId, pageable);
        return ResponseEntity.ok().body(commentResponses);
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<Void> updateComment(
        @RequestBody ProjectCommentRequest commentRequest,
        @PathVariable Long commentId,
        @PathVariable Long projectId
    ) {
        projectCommentService.updateComment(projectId, commentId, commentRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(
        @PathVariable Long projectId,
        @PathVariable Long commentId
    ) {
        projectCommentService.deleteComment(projectId, commentId);
        return ResponseEntity.ok().build();
    }
}
