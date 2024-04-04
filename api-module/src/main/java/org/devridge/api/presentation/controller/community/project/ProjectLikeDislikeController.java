package org.devridge.api.presentation.controller.community.project;

import lombok.RequiredArgsConstructor;
import org.devridge.api.application.community.project.ProjectLikeDislikeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/community/projects/{projectId}")
public class ProjectLikeDislikeController {

    private final ProjectLikeDislikeService projectLikeDislikeService;

    @PostMapping("/like")
    public ResponseEntity<Void> createProjectLike(@PathVariable Long projectId) {
        projectLikeDislikeService.createProjectLike(projectId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/dislike")
    public ResponseEntity<Void> createProjectDislike(@PathVariable Long projectId) {
        projectLikeDislikeService.createProjectDislike(projectId);
        return ResponseEntity.ok().build();
    }
}
