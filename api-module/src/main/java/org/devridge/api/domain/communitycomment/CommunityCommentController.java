package org.devridge.api.domain.communitycomment;

import java.util.ArrayList;
import java.util.List;
import org.devridge.api.githubsociallogintemp.domain.Member;
import org.devridge.common.dto.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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
        BaseResponse response = new BaseResponse(
            HttpStatus.OK.value(),
            "댓글 작성 성공"
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/read/comment/community/{communityId}")
    public ResponseEntity<?> viewComment(@PathVariable Long communityId) {
        List<CommunityComment> communityComments = communityCommentService.getAllComment(communityId);
        List<CommunityCommentResponse> dtos = new ArrayList<>();
        for (CommunityComment comment : communityComments) {
            Member member = comment.getMember(); // 댓글 작성자 정보 가져오기   //todo: 없는 맴버 안 가져오는 예외 처리
            CommunityCommentResponse communityCommentResponse = new CommunityCommentResponse(
                member.getNickname(), comment.getUpdatedAt(), comment.getContent());
            dtos.add(communityCommentResponse);
        }
        BaseResponse response = new BaseResponse(
            HttpStatus.OK.value(),
            "댓글 목록 조회 성공",
            dtos
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/delete/comment/community/{commentId}")
    public ResponseEntity<?> CommentDelete(@PathVariable Long commentId) {
        communityCommentService.deleteComment(commentId);
        BaseResponse response = new BaseResponse(
            HttpStatus.OK.value(),
            "댓글 삭제 성공"
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}