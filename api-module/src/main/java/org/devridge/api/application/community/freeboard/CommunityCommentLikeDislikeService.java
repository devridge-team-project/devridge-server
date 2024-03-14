package org.devridge.api.application.community.freeboard;

import javax.transaction.Transactional;

import lombok.RequiredArgsConstructor;

import org.devridge.api.domain.community.entity.Community;
import org.devridge.api.domain.community.entity.CommunityComment;
import org.devridge.api.domain.community.entity.CommunityCommentLikeDislike;
import org.devridge.api.domain.community.entity.LikeStatus;
import org.devridge.api.domain.community.entity.id.CommunityCommentLikeDislikeId;
import org.devridge.api.domain.community.exception.MyCommunityForbiddenException;
import org.devridge.api.infrastructure.community.freeboard.CommunityCommentLikeDislikeRepository;
import org.devridge.api.infrastructure.community.freeboard.CommunityCommentRepository;
import org.devridge.api.infrastructure.community.freeboard.CommunityRepository;
import org.devridge.api.domain.member.entity.Member;
import org.devridge.api.infrastructure.member.MemberRepository;
import org.devridge.api.common.exception.common.DataNotFoundException;
import org.devridge.api.common.util.SecurityContextHolderUtil;

import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CommunityCommentLikeDislikeService {

    private final CommunityCommentLikeDislikeRepository communityCommentLikeDislikeRepository;
    private final CommunityCommentRepository communityCommentRepository;
    private final MemberRepository memberRepository;
    private final CommunityRepository communityRepository;
    private final CommunityCommentLikeDislikeMapper communityCommentLikeDislikeMapper;

    @Transactional
    public void createCommunityCommentLike(Long communityId, Long commentId) {
        Long accessMemberId = SecurityContextHolderUtil.getMemberId();
        Member member = getMemberById(accessMemberId);
        Community community = getCommunityById(communityId);
        CommunityComment comment = getCommentById(commentId);
        CommunityCommentLikeDislikeId communityCommentLikeDislikeId =
            new CommunityCommentLikeDislikeId(member.getId(), comment.getId());

        if (accessMemberId.equals(community.getMember().getId())) {
            throw new MyCommunityForbiddenException(403, "내가 작성한 글은 추천할 수 없습니다.");
        }

        communityCommentLikeDislikeRepository.findById(communityCommentLikeDislikeId).ifPresentOrElse(
            CommunityCommentLikeDislike -> {
                LikeStatus status = CommunityCommentLikeDislike.getStatus();

                if (status == LikeStatus.G) {
                    changeIsDeletedStatus(CommunityCommentLikeDislike);
                }

                if (status == LikeStatus.B) {
                    if (CommunityCommentLikeDislike.getIsDeleted()) {
                        communityCommentLikeDislikeRepository.restoreById(communityCommentLikeDislikeId);
                    }
                    communityCommentLikeDislikeRepository.updateLikeDislike(communityCommentLikeDislikeId,
                        LikeStatus.G);
                }
            },
            () -> {
                CommunityCommentLikeDislike commentLikeDislike =
                    communityCommentLikeDislikeMapper.toCommunityCommentLikeDislike(member, comment, LikeStatus.G);
                communityCommentLikeDislikeRepository.save(commentLikeDislike);
            }
        );
        updateLikeDislike(communityCommentLikeDislikeId.getCommentId());
    }

    @Transactional
    public void createCommunityCommentDislike(Long communityId, Long commentId) {
        Long accessMemberId = SecurityContextHolderUtil.getMemberId();
        Member member = getMemberById(accessMemberId);
        Community community = getCommunityById(communityId);
        CommunityComment comment = getCommentById(commentId);
        CommunityCommentLikeDislikeId communityCommentLikeDislikeId =
            new CommunityCommentLikeDislikeId(member.getId(), comment.getId());

        if (accessMemberId.equals(community.getMember().getId())) {
            throw new MyCommunityForbiddenException(403, "내가 작성한 글은 비추천할 수 없습니다.");
        }

        communityCommentLikeDislikeRepository.findById(communityCommentLikeDislikeId).ifPresentOrElse(
            CommunityCommentLikeDislike -> {
                LikeStatus status = CommunityCommentLikeDislike.getStatus();

                if (status == LikeStatus.B) {
                    changeIsDeletedStatus(CommunityCommentLikeDislike);
                }

                if (status == LikeStatus.G) {
                    if (CommunityCommentLikeDislike.getIsDeleted()) {
                        communityCommentLikeDislikeRepository.restoreById(communityCommentLikeDislikeId);
                    }
                    communityCommentLikeDislikeRepository.updateLikeDislike(communityCommentLikeDislikeId,
                        LikeStatus.B);
                }
            },
            () -> {
                CommunityCommentLikeDislike commentLikeDislike =
                    communityCommentLikeDislikeMapper.toCommunityCommentLikeDislike(member, comment, LikeStatus.B);
                communityCommentLikeDislikeRepository.save(commentLikeDislike);
            }
        );
        updateLikeDislike(communityCommentLikeDislikeId.getCommentId());
    }

    private void updateLikeDislike(Long communityId) {
        Long likes = Long.valueOf(communityCommentLikeDislikeRepository.countCommunityLikeDislikeById(communityId, LikeStatus.G));
        Long disLikes = Long.valueOf(communityCommentLikeDislikeRepository.countCommunityLikeDislikeById(communityId, LikeStatus.B));
        communityCommentRepository.updateLikeDislike(likes, disLikes, communityId);
    }

    private void changeIsDeletedStatus(CommunityCommentLikeDislike communitycommentLikeDislike) {
        if (communitycommentLikeDislike.getIsDeleted()) {
            communityCommentLikeDislikeRepository.restoreById(communitycommentLikeDislike.getId());
        }
        if (!communitycommentLikeDislike.getIsDeleted()) {
            communityCommentLikeDislikeRepository.deleteById(communitycommentLikeDislike.getId());
        }
    }

    private Member getMemberById(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(() -> new DataNotFoundException());
    }

    private CommunityComment getCommentById(Long commentId) {
        return communityCommentRepository.findById(commentId).orElseThrow(() -> new DataNotFoundException());
    }

    private Community getCommunityById(Long communityId) {
        return communityRepository.findById(communityId).orElseThrow(() -> new DataNotFoundException());
    }
}