package org.devridge.api.domain.community.service;

import java.util.Optional;
import javax.persistence.EntityNotFoundException;
import org.devridge.api.domain.community.entity.CommunityComment;
import org.devridge.api.domain.community.entity.CommunityCommentLikeDislike;
import org.devridge.api.domain.community.repository.CommunityCommentLikeDislikeRepository;
import org.devridge.api.domain.community.entity.id.CommunityCommentLikeDislikeId;
import org.devridge.api.domain.community.entity.LikeStatus;
import org.devridge.api.domain.member.entity.Member;
import org.devridge.api.util.SecurityContextHolderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class CommunityCommentLikeDislikeService {

    private final CommunityCommentLikeDislikeRepository communityCommentLikeDislikeRepository;

    @Autowired
    public CommunityCommentLikeDislikeService(
        CommunityCommentLikeDislikeRepository communityCommentLikeDislikeRepository) {
        this.communityCommentLikeDislikeRepository = communityCommentLikeDislikeRepository;
    }

    public void createLikeDisLike(Long commentId, LikeStatus status) {
        Member member = SecurityContextHolderUtil.getMember();
        CommunityComment communityComment = CommunityComment.builder().id(commentId).build();
        CommunityCommentLikeDislike communityCommentLikeDislike = CommunityCommentLikeDislike.builder()
            .id(new CommunityCommentLikeDislikeId(member.getId(), commentId))
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

    public void changeCommunityCommentLikeDislike(Long commentId, LikeStatus status) {
        Optional<CommunityCommentLikeDislike> communityCommentLikeDislike = communityCommentLikeDislikeRepository.findById(
            new CommunityCommentLikeDislikeId(SecurityContextHolderUtil.getMemberId(), commentId));
        communityCommentLikeDislike.ifPresentOrElse(
            likeDislike -> {
                likeDislike.changeStatus(status);
                communityCommentLikeDislikeRepository.save(likeDislike);
            },
            () -> {
                throw new EntityNotFoundException("해당 엔터티를 찾을 수 없습니다.");
            });
    }
}
