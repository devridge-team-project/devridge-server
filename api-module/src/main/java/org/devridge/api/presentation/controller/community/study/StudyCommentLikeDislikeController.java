package org.devridge.api.presentation.controller.community.study;

import lombok.RequiredArgsConstructor;
import org.devridge.api.application.community.study.StudyCommentLikeDislikeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/community/studies/{studyId}/comments/{commentId}")
@RestController
public class StudyCommentLikeDislikeController {

    private final StudyCommentLikeDislikeService studyCommentLikeDislikeService;

    @PostMapping("/like")
    public ResponseEntity<Void> createStudyCommentLike(
        @PathVariable Long studyId,
        @PathVariable Long commentId
    ) {
        studyCommentLikeDislikeService.createStudyCommentLike(studyId, commentId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/dislike")
    public ResponseEntity<Void> createStudyCommentDislike(
        @PathVariable Long studyId,
        @PathVariable Long commentId
    ) {
        studyCommentLikeDislikeService.createStudyCommentDislike(studyId, commentId);
        return ResponseEntity.ok().build();
    }
}