package org.devridge.api.domain.communitycomment;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

@Service
public class CommunityCommentService {

    private CommunityCommentRepository communityCommentRepository;

    @Autowired
    public CommunityCommentService(CommunityCommentRepository communityCommentRepository) {
        this.communityCommentRepository = communityCommentRepository;
    }

    public CommunityComment createComment(Long communityId, Long memberId, String content) {
        if (communityId == null) {
            throw new IllegalArgumentException("communityId가 null입니다.");
        }

        if (memberId == null) { // todo: memberId 있는건지 조회해봐야함 memberRepository 조회?
            throw new IllegalArgumentException("memberId가 null입니다.");
        }

        if (content == null || content.isEmpty()) {
            throw new IllegalArgumentException("content가 null이거나 비어있습니다.");
        }

        CommunityComment communityComment = CommunityComment
            .builder()
            .communityId(communityId)
            .content(content)
            .memberId(memberId)
            .build();
        return communityCommentRepository.save(communityComment);
    }

    public List<CommunityComment> getAllComment(Long communityId) {
        if (communityId == null) {
            throw new IllegalArgumentException("커뮤니티 ID는 null일 수 없습니다.");
        }
        List<CommunityComment> communityComments =
            communityCommentRepository.findByCommunityId(communityId);
        return communityComments;
    }

    public void deleteComment(Long commentId) {
        try {
            communityCommentRepository.deleteById(commentId);
        } catch (EmptyResultDataAccessException e) {
            throw new EmptyResultDataAccessException("댓글을 찾을 수 없습니다.", 1);
        }
    }
}
