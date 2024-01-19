package org.devridge.api.domain.community.service;

import java.util.List;
import java.util.Optional;
import javax.persistence.EntityNotFoundException;
import org.devridge.api.domain.community.entity.CommunityComment;
import org.devridge.api.domain.community.entity.CommunityCommentLikeDislike;
import org.devridge.api.domain.community.repository.CommunityCommentRepository;
import org.devridge.api.util.SecurityContextHolderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

@Service
public class CommunityCommentService {

    private final CommunityCommentRepository communityCommentRepository;

    @Autowired
    public CommunityCommentService(CommunityCommentRepository communityCommentRepository) {
        this.communityCommentRepository = communityCommentRepository;
    }

    public void createComment(Long communityId, String content) {
        CommunityComment communityComment = CommunityComment
            .builder()
            .communityId(communityId)
            .content(content)
            .memberId(SecurityContextHolderUtil.getMemberId())
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

    public void updateComment(Long communityId, String content) {
        Optional<CommunityComment> optionalCommunity = communityCommentRepository.findById(communityId);
        optionalCommunity.ifPresentOrElse(
            community -> {
                if (!community.getMemberId().equals(SecurityContextHolderUtil.getMemberId())) {
                    throw new AccessDeniedException("거부된 접근입니다.");
                }
                community.updateComment(content);
                communityCommentRepository.save(community);

            },
            () -> {
                throw new EntityNotFoundException("해당 엔티티를 찾을 수 없습니다.");
            });
    }

    public void deleteComment(Long commentId) {
        communityCommentRepository.findById(commentId).ifPresentOrElse(
            community -> {
                if (!community.getMemberId().equals(SecurityContextHolderUtil.getMemberId())) {
                    throw new AccessDeniedException("거부된 접근입니다.");
                }
                communityCommentRepository.deleteById(commentId);
            },
            () -> {
                throw new EntityNotFoundException("삭제할 댓글을 찾을 수 없습니다.");
            }
        );
    }

    public void updateLikeDislike(Long commentId) {
        CommunityComment communityComment = communityCommentRepository.findById(commentId).orElseThrow();
        List<CommunityCommentLikeDislike> list = communityComment.getCommunityCommentLikeDislike();
        communityComment.countLikeDislike(list);
        communityCommentRepository.save(communityComment);
    }
}
