package org.devridge.api.domain.community.service;

import java.util.Optional;
import javax.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.devridge.api.domain.community.entity.CommunityCommentLikeDislike;
import org.devridge.api.domain.community.entity.LikeStatus;
import org.devridge.api.domain.community.entity.id.CommunityCommentLikeDislikeId;
import org.devridge.api.domain.community.repository.CommunityCommentLikeDislikeRepository;
import org.devridge.api.util.SecurityContextHolderUtil;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CommunityCommentLikeDislikeService {

    private final CommunityCommentLikeDislikeRepository communityCommentLikeDislikeRepository;

    public void createLikeDisLike(Long commentId, LikeStatus status) {
        Long memberId = SecurityContextHolderUtil.getMemberId();
        CommunityCommentLikeDislike communityCommentLikeDislike = CommunityCommentLikeDislike.builder()
            .id(new CommunityCommentLikeDislikeId(memberId, commentId))
            .status(status)
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
