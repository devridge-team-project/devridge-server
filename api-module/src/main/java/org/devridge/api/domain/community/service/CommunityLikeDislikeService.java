package org.devridge.api.domain.community.service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.devridge.api.domain.community.entity.Community;
import org.devridge.api.domain.community.entity.CommunityLikeDislike;
import org.devridge.api.domain.community.entity.LikeStatus;
import org.devridge.api.domain.community.entity.id.CommunityLikeDislikeId;
import org.devridge.api.domain.community.exception.MyCommunityForbiddenException;
import org.devridge.api.domain.community.mapper.CommunityLikeDislikeMapper;
import org.devridge.api.domain.community.repository.CommunityLikeDislikeRepository;
import org.devridge.api.domain.community.repository.CommunityRepository;
import org.devridge.api.domain.member.entity.Member;
import org.devridge.api.domain.member.repository.MemberRepository;
import org.devridge.api.util.SecurityContextHolderUtil;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommunityLikeDislikeService {

    private final CommunityLikeDislikeRepository communityLikeDislikeRepository;
    private final CommunityRepository communityRepository;
    private final MemberRepository memberRepository;
    private final CommunityLikeDislikeMapper communityLikeDislikeMapper;

    @Transactional
    public void createCommunityLike(Long communityId) {
        Long accessMemberId = SecurityContextHolderUtil.getMemberId();
        Member member = getMemberById(accessMemberId);
        Community community = getCommunityById(communityId);
        CommunityLikeDislikeId communityLikeDislikeId = new CommunityLikeDislikeId(member.getId(), community.getId());

        if (accessMemberId.equals(community.getMember().getId())) {
            throw new MyCommunityForbiddenException(403, "내가 작성한 글은 추천할 수 없습니다.");
        }

        communityLikeDislikeRepository.findById(communityLikeDislikeId).ifPresentOrElse(
            communityLikeDislike -> {
                LikeStatus status = communityLikeDislike.getStatus();

                if (status == LikeStatus.G) {
                    changeIsDeletedStatus(communityLikeDislike);
                }

                if (status == LikeStatus.B) {
                    if (communityLikeDislike.getIsDeleted()) {
                        communityLikeDislikeRepository.restoreById(communityLikeDislikeId);
                    }
                    communityLikeDislikeRepository.updateLikeDislike(communityLikeDislikeId, LikeStatus.G);
                }
            },
            () -> {
                CommunityLikeDislike communityLikeDislike
                    = communityLikeDislikeMapper.toCommunityLikeDislike(community, member, LikeStatus.G);
                communityLikeDislikeRepository.save(communityLikeDislike);
            }
        );
        updateLikeDislike(communityLikeDislikeId.getCommunityId());
    }

    @Transactional
    public void createCommunityDislike(Long communityId) {
        Long accessMemberId = SecurityContextHolderUtil.getMemberId();
        Member member = getMemberById(accessMemberId);
        Community community = getCommunityById(communityId);
        CommunityLikeDislikeId communityLikeDislikeId = new CommunityLikeDislikeId(member.getId(), community.getId());

        if (accessMemberId.equals(community.getMember().getId())) {
            throw new MyCommunityForbiddenException(403, "내가 작성한 글은 비추천할 수 없습니다.");
        }

        communityLikeDislikeRepository.findById(communityLikeDislikeId).ifPresentOrElse(
            communityLikeDislike -> {
                LikeStatus status = communityLikeDislike.getStatus();

                if (status == LikeStatus.G) {
                    if (communityLikeDislike.getIsDeleted()) {
                        communityLikeDislikeRepository.restoreById(communityLikeDislikeId);
                    }
                    communityLikeDislikeRepository.updateLikeDislike(communityLikeDislikeId, LikeStatus.B);
                }

                if (status == LikeStatus.B) {
                    changeIsDeletedStatus(communityLikeDislike);
                }
            },
            () -> {
                CommunityLikeDislike communityLikeDislike
                    = communityLikeDislikeMapper.toCommunityLikeDislike(community, member, LikeStatus.B);
                communityLikeDislikeRepository.save(communityLikeDislike);
            }
        );
        updateLikeDislike(communityLikeDislikeId.getCommunityId());
    }

    private void changeIsDeletedStatus(CommunityLikeDislike communityLikeDislike) {
        if (communityLikeDislike.getIsDeleted()) {
            communityLikeDislikeRepository.restoreById(communityLikeDislike.getId());
        }
        if (!communityLikeDislike.getIsDeleted()) {
            communityLikeDislikeRepository.deleteById(communityLikeDislike.getId());
        }
    }

    private Member getMemberById(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(() -> new EntityNotFoundException());
    }

    private Community getCommunityById(Long communityId) {
        return communityRepository.findById(communityId).orElseThrow(() -> new EntityNotFoundException());
    }

    private void updateLikeDislike(Long communityId) {
        Long likes = Long.valueOf(communityLikeDislikeRepository.countCommunityLikeDislikeByCommunityId(communityId, LikeStatus.G));
        Long disLikes = Long.valueOf(communityLikeDislikeRepository.countCommunityLikeDislikeByCommunityId(communityId, LikeStatus.B));
        communityRepository.updateLikeDislike(likes, disLikes, communityId);
    }
}
