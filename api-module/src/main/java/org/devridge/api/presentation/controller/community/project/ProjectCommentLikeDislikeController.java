package org.devridge.api.presentation.controller.community.project;

import lombok.RequiredArgsConstructor;
import org.devridge.api.application.community.project.ProjectCommentLikeDislikeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/community/projects/{projectId}/comments/{commentId}")
@RestController
public class ProjectCommentLikeDislikeController {

    private final ProjectCommentLikeDislikeService projectCommentLikeDislikeService;

    @PostMapping("/like")
    public ResponseEntity<Void> createProjectCommentLike(
        @PathVariable Long projectId,
        @PathVariable Long commentId
    ) {
        projectCommentLikeDislikeService.createProjectCommentLike(projectId, commentId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/dislike")
    public ResponseEntity<Void> createProjectCommentDislike(
        @PathVariable Long projectId,
        @PathVariable Long commentId
    ) {
        projectCommentLikeDislikeService.createProjectCommentDislike(projectId, commentId);
        return ResponseEntity.ok().build();
    }
}