package org.devridge.api.domain.community.service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.devridge.api.domain.community.entity.Community;
import org.devridge.api.domain.community.entity.CommunityLikeDislike;
import org.devridge.api.domain.community.entity.LikeStatus;
import org.devridge.api.domain.community.entity.id.CommunityLikeDislikeId;
import org.devridge.api.domain.community.mapper.CommunityLikeDislikeMapper;
import org.devridge.api.domain.community.repository.CommunityLikeDislikeRepository;
import org.devridge.api.domain.community.repository.CommunityRepository;
import org.devridge.api.domain.member.entity.Member;
import org.devridge.api.domain.member.repository.MemberRepository;
import org.devridge.api.util.SecurityContextHolderUtil;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.AccessDeniedException;
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

        if (!accessMemberId.equals(community.getMember().getId())) {
            throw new AccessDeniedException("거부된 접근입니다.");
        }

        communityLikeDislikeRepository.findById(communityLikeDislikeId).ifPresentOrElse(
            communityLikeDislike -> {
                if (!communityLikeDislike.getIsDeleted()) {
                    if (communityLikeDislike.getStatus() == LikeStatus.G) {
                        throw new DataIntegrityViolationException("이미 Like상태입니다.");
                    }

                    if (communityLikeDislike.getStatus() == LikeStatus.B) {
                        communityLikeDislikeRepository.updateLikeDislike(communityLikeDislikeId, LikeStatus.G);
                    }
                }

                if (communityLikeDislike.getIsDeleted()) {
                    communityLikeDislikeRepository.restoreById(communityLikeDislikeId);

                    if (communityLikeDislike.getStatus() == LikeStatus.B) {
                        communityLikeDislikeRepository.updateLikeDislike(communityLikeDislikeId, LikeStatus.G);
                    }
                }
            },
            () -> {
                CommunityLikeDislike communityLikeDislike
                    = communityLikeDislikeMapper.toCommunityLikeDislike(community, member, LikeStatus.G);
                communityLikeDislikeRepository.save(communityLikeDislike);
            }
        );
    }

    public void deleteCommunityLike(Long communityId) {
        Long accessMemberId = SecurityContextHolderUtil.getMemberId();
        Member member = getMemberById(accessMemberId);
        Community community = getCommunityById(communityId);
        CommunityLikeDislikeId communityLikeDislikeId = new CommunityLikeDislikeId(member.getId(), community.getId());

        if (!accessMemberId.equals(community.getMember().getId())) {
            throw new AccessDeniedException("거부된 접근입니다.");
        }

        communityLikeDislikeRepository.findById(communityLikeDislikeId).ifPresentOrElse(
            communityLikeDislike -> {
                if (communityLikeDislike.getIsDeleted()) {
                    throw new EntityNotFoundException("삭제할 Like가 없습니다.");
                }

                if (communityLikeDislike.getStatus() == LikeStatus.B) {
                    throw new DataIntegrityViolationException("삭제할 Like가 없습니다.");
                }

                if (communityLikeDislike.getStatus() == LikeStatus.G) {
                    communityLikeDislikeRepository.deleteById(communityLikeDislikeId);
                }
            },
            () -> {
                throw new EntityNotFoundException("엔티티가 존재하지 않습니다.");
            }
        );
    }

    @Transactional
    public void createCommunityDislike(Long communityId) {
        Long accessMemberId = SecurityContextHolderUtil.getMemberId();
        Member member = getMemberById(accessMemberId);
        Community community = getCommunityById(communityId);
        CommunityLikeDislikeId communityLikeDislikeId = new CommunityLikeDislikeId(member.getId(), community.getId());

        if (!accessMemberId.equals(community.getMember().getId())) {
            throw new AccessDeniedException("거부된 접근입니다.");
        }

        communityLikeDislikeRepository.findById(communityLikeDislikeId).ifPresentOrElse(
            communityLikeDislike -> {
                if (!communityLikeDislike.getIsDeleted()) {
                    if (communityLikeDislike.getStatus() == LikeStatus.B) {
                        throw new DataIntegrityViolationException("이미 Dislike상태입니다.");
                    }

                    if (communityLikeDislike.getStatus() == LikeStatus.G) {
                        communityLikeDislikeRepository.updateLikeDislike(communityLikeDislikeId, LikeStatus.B);
                    }
                }

                if (communityLikeDislike.getIsDeleted()) {
                    communityLikeDislikeRepository.restoreById(communityLikeDislikeId);

                    if (communityLikeDislike.getStatus() == LikeStatus.G) {
                        communityLikeDislikeRepository.updateLikeDislike(communityLikeDislikeId, LikeStatus.B);
                    }
                }
            },
            () -> {
                CommunityLikeDislike communityLikeDislike
                    = communityLikeDislikeMapper.toCommunityLikeDislike(community, member, LikeStatus.B);
                communityLikeDislikeRepository.save(communityLikeDislike);
            }
        );
    }

    public void deleteCommunityDislike(Long communityId) {
        Long accessMemberId = SecurityContextHolderUtil.getMemberId();
        Member member = getMemberById(accessMemberId);
        Community community = getCommunityById(communityId);
        CommunityLikeDislikeId communityLikeDislikeId = new CommunityLikeDislikeId(member.getId(), community.getId());

        if (!accessMemberId.equals(community.getMember().getId())) {
            throw new AccessDeniedException("거부된 접근입니다.");
        }

        communityLikeDislikeRepository.findById(communityLikeDislikeId).ifPresentOrElse(
            communityLikeDislike -> {
                if (communityLikeDislike.getIsDeleted()) {
                    throw new EntityNotFoundException("삭제할 Dislike가 없습니다.");
                }

                if (communityLikeDislike.getStatus() == LikeStatus.G) {
                    throw new DataIntegrityViolationException("삭제할 Dislike가 없습니다.");
                }

                if (communityLikeDislike.getStatus() == LikeStatus.B) {
                    communityLikeDislikeRepository.deleteById(communityLikeDislikeId);
                }
            },
            () -> {
                throw new EntityNotFoundException("엔티티가 존재하지 않습니다.");
            }
        );
    }

    private Member getMemberById(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(() -> new EntityNotFoundException());
    }

    private Community getCommunityById(Long communityId) {
        return communityRepository.findById(communityId).orElseThrow(() -> new EntityNotFoundException());
    }
}
