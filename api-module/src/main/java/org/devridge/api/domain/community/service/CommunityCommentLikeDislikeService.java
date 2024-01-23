package org.devridge.api.domain.community.service;

import javax.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.devridge.api.domain.community.entity.Community;
import org.devridge.api.domain.community.entity.CommunityComment;
import org.devridge.api.domain.community.entity.CommunityCommentLikeDislike;
import org.devridge.api.domain.community.entity.LikeStatus;
import org.devridge.api.domain.community.entity.id.CommunityCommentLikeDislikeId;
import org.devridge.api.domain.community.repository.CommunityCommentLikeDislikeRepository;
import org.devridge.api.domain.community.repository.CommunityCommentRepository;
import org.devridge.api.domain.community.repository.CommunityQuerydslRepository;
import org.devridge.api.domain.community.repository.CommunityRepository;
import org.devridge.api.domain.member.entity.Member;
import org.devridge.api.domain.member.repository.MemberRepository;
import org.devridge.api.util.SecurityContextHolderUtil;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CommunityCommentLikeDislikeService {

    private final CommunityCommentLikeDislikeRepository communityCommentLikeDislikeRepository;
    private final CommunityCommentRepository communityCommentRepository;
    private final MemberRepository memberRepository;
    private final CommunityRepository communityRepository;
    private final CommunityQuerydslRepository communityQuerydslRepository;

    public void createLikeDisLike(Long communityId, Long commentId, LikeStatus status) {
        Long memberId = SecurityContextHolderUtil.getMemberId();
        Member member = getMemberById(memberId);
        getCommunityById(communityId);
        CommunityComment comment = getCommentById(commentId);

        if (communityCommentLikeDislikeRepository.findById(
            new CommunityCommentLikeDislikeId(member.getId(), comment.getId())).isPresent()) {
            throw new DataIntegrityViolationException("이미 존재하는 데이터입니다.");
        }

        CommunityCommentLikeDislike commentLikeDislike = CommunityCommentLikeDislike.builder()
            .communityComment(comment)
            .member(member)
            .status(status)
            .build();
        communityCommentLikeDislikeRepository.save(commentLikeDislike);
    }

    public void changeCommunityCommentLikeDislike(Long communityId, Long commentId, LikeStatus status) {
        Long memberId = SecurityContextHolderUtil.getMemberId();
        Member member = getMemberById(memberId);
        getCommunityById(communityId);
        CommunityComment comment = getCommentById(commentId);
        CommunityCommentLikeDislike CommentLikeDislike = communityCommentLikeDislikeRepository.findById(
                new CommunityCommentLikeDislikeId(member.getId(), comment.getId()))
            .orElseThrow(() -> new EntityNotFoundException());
        CommentLikeDislike.changeStatus(status);
        communityCommentLikeDislikeRepository.save(CommentLikeDislike);
    }

    public void updateLikeDislike(Long commentId) {
        getCommentById(commentId);
        communityQuerydslRepository.updateLikeCountByCommentId(commentId);
    }

    public Member getMemberById(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(() -> new EntityNotFoundException());
    }

    public CommunityComment getCommentById(Long commentId) {
        return communityCommentRepository.findById(commentId).orElseThrow(() -> new EntityNotFoundException());
    }

    public Community getCommunityById(Long communityId) {
        return communityRepository.findById(communityId).orElseThrow(() -> new EntityNotFoundException());
    }
}
