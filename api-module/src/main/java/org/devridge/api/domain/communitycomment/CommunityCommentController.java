package org.devridge.api.domain.communitycomment;

import java.util.ArrayList;
import java.util.List;
import org.devridge.api.githubsociallogintemp.domain.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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

    @GetMapping("/read/comment/community/{communityId}")
    public ResponseEntity<?> viewComment(@PathVariable Long communityId) {
        List<CommunityComment> communityComments
            = communityCommentService.getAllComment(communityId);
        List<CommunityCommentResponse> dto = new ArrayList<>();

        for (CommunityComment comment : communityComments) {
            Member member = comment.getMember(); // 댓글 작성자 정보 가져오기   //todo: 없는 맴버 안 가져오는 예외 처리
            CommunityCommentResponse communityCommentResponse = new CommunityCommentResponse(
                member.getNickname(), comment.getUpdatedAt());
            dto.add(communityCommentResponse);
        }
        return ResponseEntity.ok(dto);
    }
}
