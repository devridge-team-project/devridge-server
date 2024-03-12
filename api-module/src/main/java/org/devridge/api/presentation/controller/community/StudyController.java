package org.devridge.api.presentation.controller.community;

import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.devridge.api.domain.community.dto.request.StudyRequest;
import org.devridge.api.domain.community.dto.response.StudyDetailResponse;
import org.devridge.api.domain.community.dto.response.StudyListResponse;
import org.devridge.api.application.community.StudyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/community/studies")
public class StudyController {

    private final StudyService studyService;

    @PostMapping
    public ResponseEntity<Void> createStudy(@RequestBody StudyRequest studyRequest) {
        Long studyId = studyService.createStudy(studyRequest);
        return ResponseEntity.created(URI.create("/api/community/studies" + studyId)).build();
    }

    @GetMapping("/{studyId}")
    public ResponseEntity<StudyDetailResponse> getStudyDetail(@PathVariable Long studyId) {
        StudyDetailResponse studyDetailResponse = studyService.getStudyDetail(studyId);
        return ResponseEntity.ok().body(studyDetailResponse);
    }

    @GetMapping
    public ResponseEntity<List<StudyListResponse>> getAllStudy() {
        List<StudyListResponse> studyListResponses = studyService.getAllStudy();
        return ResponseEntity.ok().body(studyListResponses);
    }

    @PutMapping("/{studyId}")
    public ResponseEntity<Void> updateStudy(
        @PathVariable Long studyId,
        @RequestBody StudyRequest request
    ) {
        studyService.updateStudy(studyId, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{studyId}")
    public ResponseEntity<Void> deleteStudy(@PathVariable Long studyId) {
        studyService.deleteStudy(studyId);
        return ResponseEntity.ok().build();
    }
}
