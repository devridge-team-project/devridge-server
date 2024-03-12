package org.devridge.api.domain.community.service;

import lombok.RequiredArgsConstructor;
import org.devridge.api.domain.community.dto.request.CommunityCommentRequest;
import org.devridge.api.domain.community.dto.response.CommunityCommentResponse;
import org.devridge.api.domain.community.entity.Community;
import org.devridge.api.domain.community.entity.CommunityComment;
import org.devridge.api.domain.community.exception.MyCommunityForbiddenException;
import org.devridge.api.domain.community.mapper.CommunityCommentMapper;
import org.devridge.api.domain.community.repository.CommunityCommentQuerydslReopsitory;
import org.devridge.api.domain.community.repository.CommunityCommentRepository;
import org.devridge.api.domain.community.repository.CommunityRepository;
import org.devridge.api.domain.member.entity.Member;
import org.devridge.api.domain.member.repository.MemberRepository;
import org.devridge.api.common.exception.common.DataNotFoundException;
import org.devridge.api.common.util.SecurityContextHolderUtil;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CommunityCommentService {

    private final CommunityCommentRepository communityCommentRepository;
    private final CommunityCommentMapper communityCommentMapper;
    private final CommunityRepository communityRepository;
    private final MemberRepository memberRepository;
    private final CommunityCommentQuerydslReopsitory communityCommentQuerydslReopsitory;

    public Long createComment(Long communityId, CommunityCommentRequest commentRequest) {
        Community community = getCommunityById(communityId);
        Long accessMemberId = SecurityContextHolderUtil.getMemberId();
        Member member = getMemberById(accessMemberId);

        CommunityComment communityComment =
            communityCommentMapper.toCommunityComment(community, member, commentRequest);
        return communityCommentRepository.save(communityComment).getId();
    }

    public Slice<CommunityCommentResponse> getAllCommunityComment(Long communityId, Long lastId, Pageable pageable) {
        return communityCommentQuerydslReopsitory.searchBySlice(communityId, lastId, pageable);
    }

    public void updateComment(Long communityId, Long commentId, CommunityCommentRequest commentRequest) {
        getCommunityById(communityId);
        Long accessMemberId = SecurityContextHolderUtil.getMemberId();
        getMemberById(accessMemberId);
        CommunityComment comment = getCommunityComment(commentId);

        if (!comment.getMember().getId().equals(accessMemberId)) {
            throw new MyCommunityForbiddenException(403, "내가 작성하지 않은 글은 수정할 수 없습니다.");
        }

        comment.updateComment(commentRequest.getContent());
        communityCommentRepository.save(comment);
    }

    public void deleteComment(Long communityId, Long commentId) {
        getCommunityById(communityId);
        Long accessMemberId = SecurityContextHolderUtil.getMemberId();
        getMemberById(accessMemberId);
        CommunityComment comment = getCommunityComment(commentId);

        if (!comment.getMember().getId().equals(accessMemberId)) {
            throw new MyCommunityForbiddenException(403, "내가 작성하지 않은 글은 삭제할 수 없습니다.");
        }

        communityCommentRepository.deleteById(commentId);
    }

    private CommunityComment getCommunityComment(Long commentId) {
        return communityCommentRepository.findById(commentId).orElseThrow(() -> new DataNotFoundException());
    }

    private Community getCommunityById(Long communityId) {
        return communityRepository.findById(communityId).orElseThrow(() -> new DataNotFoundException());
    }

    private Member getMemberById(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(() -> new DataNotFoundException());
    }

}