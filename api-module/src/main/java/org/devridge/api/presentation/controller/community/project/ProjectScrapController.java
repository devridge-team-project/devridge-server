package org.devridge.api.presentation.controller.community.project;

import lombok.RequiredArgsConstructor;
import org.devridge.api.application.community.project.ProjectScrapService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/community/projects/{projectId}/scraps")
@RestController
public class ProjectScrapController {

    private final ProjectScrapService projectScrapService;

    @PostMapping
    public ResponseEntity<Void> createScrap(@PathVariable Long projectId) {
        projectScrapService.createScrap(projectId);
        return ResponseEntity.ok().build();
    }
}