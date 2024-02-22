package org.devridge.api.domain.community.controller;

import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.devridge.api.domain.community.dto.request.StudyRequest;
import org.devridge.api.domain.community.service.StudyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
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
}
