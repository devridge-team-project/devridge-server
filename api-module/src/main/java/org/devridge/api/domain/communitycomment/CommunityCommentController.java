package org.devridge.api.domain.communitycomment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CommunityCommentController {

    private CommunityCommentService communityCommentService;

    @Autowired
    public CommunityCommentController(CommunityCommentService communityCommentService) {
        this.communityCommentService = communityCommentService;
    }

    @PostMapping("/write/comment/community/{communityId}/{memberId}") //todo: memberId 검증방법
    public ResponseEntity<?> writeComment(@RequestBody CommunityCommentRequest dto,
        @PathVariable Long communityId, @PathVariable Long memberId) {
        communityCommentService.createComment(communityId, memberId, dto.getContent());
        return ResponseEntity.ok().build();
    }
}
