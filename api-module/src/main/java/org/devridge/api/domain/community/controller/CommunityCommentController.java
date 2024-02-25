package org.devridge.api.domain.community.controller;

import java.net.URI;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.devridge.api.domain.community.dto.request.CommunityCommentRequest;
import org.devridge.api.domain.community.dto.response.CommunityCommentResponse;
import org.devridge.api.domain.community.service.CommunityCommentService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
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

@RequiredArgsConstructor
@RequestMapping("/api/community/{communityId}/comments")
@RestController
public class CommunityCommentController {

    private final CommunityCommentService communityCommentService;

    @PostMapping
    public ResponseEntity<Void> createComment(
        @Valid @RequestBody CommunityCommentRequest commentRequest,
        @PathVariable Long communityId
    ) {
        Long commentId = communityCommentService.createComment(communityId, commentRequest);
        return ResponseEntity.created(URI.create("/api/community/" + communityId + "/comments/" + commentId)).build();
    }

//    @GetMapping
//    public ResponseEntity<List<CommunityCommentResponse>> getAllComments(@PathVariable Long communityId) {
//        List<CommunityCommentResponse> commentResponses = communityCommentService.getAllComment(communityId);
//        return ResponseEntity.ok().body(commentResponses);
//    }

    @GetMapping
    public ResponseEntity<Slice<CommunityCommentResponse>> getAllComments(
        @PathVariable Long communityId,
        @RequestParam(name = "lastId", required = false) Long lastId,
        @PageableDefault( ) Pageable pageable
    ) {
        Slice<CommunityCommentResponse> commentResponses = communityCommentService.getAllCommunityComment(communityId, lastId, pageable);
        return ResponseEntity.ok().body(commentResponses);
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<Void> updateComment(
        @RequestBody CommunityCommentRequest commentRequest,
        @PathVariable Long commentId,
        @PathVariable Long communityId
    ) {
        communityCommentService.updateComment(communityId, commentId, commentRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(
        @PathVariable Long communityId,
        @PathVariable Long commentId
    ) {
        communityCommentService.deleteComment(communityId, commentId);
        return ResponseEntity.ok().build();
    }
}
