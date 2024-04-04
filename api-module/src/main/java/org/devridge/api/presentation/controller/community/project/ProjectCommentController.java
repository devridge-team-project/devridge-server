package org.devridge.api.presentation.controller.community.project;

import java.net.URI;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.devridge.api.application.community.project.ProjectCommentService;
import org.devridge.api.domain.community.dto.request.ProjectCommentRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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

    @PutMapping("/{commentId}")
    public ResponseEntity<Void> updateComment(
        @RequestBody ProjectCommentRequest commentRequest,
        @PathVariable Long commentId,
        @PathVariable Long projectId
    ) {
        projectCommentService.updateComment(projectId, commentId, commentRequest);
        return ResponseEntity.ok().build();
    }
}
