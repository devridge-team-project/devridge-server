package org.devridge.api.presentation.controller.community.project;

import java.net.URI;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.devridge.api.domain.community.dto.request.ProjectRequest;
import org.devridge.api.domain.community.dto.response.ProjectDetailResponse;
import org.devridge.api.domain.community.dto.response.ProjectListResponse;
import org.devridge.api.application.community.project.ProjectService;
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

@RestController
@RequestMapping("/api/community/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping
    public ResponseEntity<Void> createProject(@Valid @RequestBody ProjectRequest request) {
        Long projectId = projectService.createProject(request);
        return ResponseEntity.created(URI.create("/api/community/projects/" + projectId)).build();
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<ProjectDetailResponse> getProjectDetail(@PathVariable Long projectId) {
        ProjectDetailResponse projectDetailResponse = projectService.getProjectDetail(projectId);
        return ResponseEntity.ok().body(projectDetailResponse);
    }

    @GetMapping
    public ResponseEntity<Slice<ProjectListResponse>> getAllProject(
        @RequestParam(name = "lastId", required = false) Long lastId,
        Pageable pageable
    ) {
        Slice<ProjectListResponse> projectListResponses = projectService.getAllProject(lastId, pageable);
        return ResponseEntity.ok().body(projectListResponses);
    }

    @PutMapping("/{projectId}")
    public ResponseEntity<Void> updateProject(
        @PathVariable Long projectId,
        @RequestBody ProjectRequest request
    ) {
        projectService.updateProject(projectId, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{projectId}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long projectId) {
        projectService.deleteProject(projectId);
        return ResponseEntity.ok().build();
    }
}
