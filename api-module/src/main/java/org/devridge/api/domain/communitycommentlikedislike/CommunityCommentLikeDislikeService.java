package org.devridge.api.domain.communitycommentlikedislike;

import java.util.Optional;
import javax.persistence.EntityNotFoundException;
import org.devridge.api.domain.communitycomment.CommunityComment;
import org.devridge.api.githubsociallogintemp.domain.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class CommunityCommentLikeDislikeService {

    private CommunityCommentLikeDislikeRepository communityCommentLikeDislikeRepository;

    @Autowired
    public CommunityCommentLikeDislikeService(
        CommunityCommentLikeDislikeRepository communityCommentLikeDislikeRepository) {
        this.communityCommentLikeDislikeRepository = communityCommentLikeDislikeRepository;
    }

    public void createLikeDisLike(Long memberId, Long commentId, LikeStatus status) {
        Member member = Member.builder().id(memberId).build();
        CommunityComment communityComment = CommunityComment.builder().id(commentId).build();
        CommunityCommentLikeDislike communityCommentLikeDislike = CommunityCommentLikeDislike.builder()
            .id(new CommunityCommentLikeDislikeId(memberId, commentId))
            .status(status)
            .member(member)
            .communityComment(communityComment)
            .build();
        try {
            communityCommentLikeDislikeRepository.save(communityCommentLikeDislike);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("이미 존재하는 데이터입니다.");
        }
    }

    public void changeCommunityCommentLikeDislike(Long memberId, Long commentId,
        LikeStatus status) {
        Optional<CommunityCommentLikeDislike> communityCommentLikeDislike = communityCommentLikeDislikeRepository.findById(
            new CommunityCommentLikeDislikeId(memberId, commentId));
        communityCommentLikeDislike.ifPresentOrElse(
            likeDislike -> {
                likeDislike.setStatus(status);
                communityCommentLikeDislikeRepository.save(likeDislike);
            },
            () -> {
                throw new EntityNotFoundException("해당 엔터티를 찾을 수 없습니다.");
            });
    }
}
