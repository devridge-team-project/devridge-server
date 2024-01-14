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

    public void createComment(Long communityId, Long memberId, String content) {
        CommunityComment communityComment = CommunityComment
            .builder()
            .communityId(communityId)
            .content(content)
            .memberId(memberId)
            .build();
        communityCommentRepository.save(communityComment);
    }

    public List<CommunityComment> getAllComment(Long communityId) {
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
