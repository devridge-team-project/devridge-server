package org.devridge.api.domain.communitycomment;

import java.util.List;
import javax.persistence.EntityNotFoundException;
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
        if (communityComments.isEmpty()) {
            throw new EntityNotFoundException("해당 엔티티를 찾을 수 없습니다.");
        }
        return communityComments;
    }

    public void deleteComment(Long commentId) {
        try {
            communityCommentRepository.deleteById(commentId);
        } catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException("삭제할 댓글을 찾을 수 없습니다.");
        }
    }
}
