package org.devridge.api.presentation.controller.community.study;

import lombok.RequiredArgsConstructor;
import org.devridge.api.application.community.study.StudyLikeDislikeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/community/studies/{studyId}")
public class StudyLikeDislikeController {

    private final StudyLikeDislikeService studyLikeDislikeService;

    @PostMapping("/like")
    public ResponseEntity<Void> createStudyLike(@PathVariable Long studyId) {
        studyLikeDislikeService.createStudyLike(studyId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/dislike")
    public ResponseEntity<Void> createStudyDislike(@PathVariable Long studyId) {
        studyLikeDislikeService.createStudyDislike(studyId);
        return ResponseEntity.ok().build();
    }
}
