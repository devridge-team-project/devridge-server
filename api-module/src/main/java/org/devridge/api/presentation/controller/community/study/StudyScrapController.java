package org.devridge.api.presentation.controller.community.study;

import lombok.RequiredArgsConstructor;
import org.devridge.api.application.community.study.StudyScrapService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/community/studies/{studyId}/scraps")
@RestController
public class StudyScrapController {

    private final StudyScrapService studyScrapService;

    @PostMapping
    public ResponseEntity<Void> createScrap(@PathVariable Long studyId) {
        studyScrapService.createScrap(studyId);
        return ResponseEntity.ok().build();
    }
}