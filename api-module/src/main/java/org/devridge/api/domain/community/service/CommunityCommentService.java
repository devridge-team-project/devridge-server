package org.devridge.api.domain.community.service;

import java.util.List;
import javax.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.devridge.api.domain.community.dto.request.CommunityCommentRequest;
import org.devridge.api.domain.community.dto.response.CommunityCommentResponse;
import org.devridge.api.domain.community.entity.Community;
import org.devridge.api.domain.community.entity.CommunityComment;
import org.devridge.api.domain.community.mapper.CommunityCommentMapper;
import org.devridge.api.domain.community.repository.CommunityCommentRepository;
import org.devridge.api.domain.community.repository.CommunityRepository;
import org.devridge.api.domain.member.entity.Member;
import org.devridge.api.domain.member.repository.MemberRepository;
import org.devridge.api.util.SecurityContextHolderUtil;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CommunityCommentService {

    private final CommunityCommentRepository communityCommentRepository;
    private final CommunityCommentMapper communityCommentMapper;
    private final CommunityRepository communityRepository;
    private final MemberRepository memberRepository;

    public void createComment(Long communityId, CommunityCommentRequest commentRequest) {
        Community community = getCommunityById(communityId);
        Long memberId = SecurityContextHolderUtil.getMemberId();
        Member member = getMemberById(memberId);
        CommunityComment communityComment =
            communityCommentMapper.toCommunityComment(community, member, commentRequest);
        communityCommentRepository.save(communityComment);
    }

    public List<CommunityCommentResponse> getAllComment(Long communityId) {
        List<CommunityComment> communityComments = communityCommentRepository.findByCommunityId(communityId);
        if (communityComments.isEmpty()) {
            throw new EntityNotFoundException("해당 엔티티를 찾을 수 없습니다.");
        }
        return communityCommentMapper.toCommentResponses(communityComments);
    }

    public void updateComment(Long communityId, Long commentId, CommunityCommentRequest commentRequest) {
        getCommunityById(communityId);
        Long memberId = SecurityContextHolderUtil.getMemberId();
        getMemberById(memberId);

        CommunityComment comment = getCommunityComment(commentId);
        if (!comment.getMember().getId().equals(memberId)) {
            throw new AccessDeniedException("거부된 접근입니다.");
        }
        comment.updateComment(commentRequest.getContent());
        communityCommentRepository.save(comment);
    }

    public void deleteComment(Long communityId, Long commentId) {
        getCommunityById(communityId);
        Long memberId = SecurityContextHolderUtil.getMemberId();
        getMemberById(memberId);

        CommunityComment comment = getCommunityComment(commentId);
        if (!comment.getMember().getId().equals(memberId)) {
            throw new AccessDeniedException("거부된 접근입니다.");
        }
        communityCommentRepository.deleteById(commentId);
    }

    private CommunityComment getCommunityComment(Long commentId) {
        return communityCommentRepository.findById(commentId).orElseThrow(() -> new EntityNotFoundException());
    }

    private Community getCommunityById(Long communityId) {
        return communityRepository.findById(communityId).orElseThrow(() -> new EntityNotFoundException());
    }

    private Member getMemberById(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(() -> new EntityNotFoundException());
    }

}
