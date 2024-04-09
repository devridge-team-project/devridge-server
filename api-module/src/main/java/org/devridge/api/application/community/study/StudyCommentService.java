package org.devridge.api.application.community.study;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.devridge.api.common.exception.common.DataNotFoundException;
import org.devridge.api.common.util.SecurityContextHolderUtil;
import org.devridge.api.domain.community.dto.request.StudyCommentRequest;
import org.devridge.api.domain.community.dto.response.StudyCommentResponse;
import org.devridge.api.domain.community.entity.Study;
import org.devridge.api.domain.community.entity.StudyComment;
import org.devridge.api.domain.community.exception.MyCommunityForbiddenException;
import org.devridge.api.domain.member.entity.Member;
import org.devridge.api.infrastructure.community.study.StudyCommentQuerydslRepository;
import org.devridge.api.infrastructure.community.study.StudyCommentRepository;
import org.devridge.api.infrastructure.community.study.StudyRepository;
import org.devridge.api.infrastructure.member.MemberRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class StudyCommentService {

    private final StudyCommentRepository studyCommentRepository;
    private final StudyCommentMapper studyCommentMapper;
    private final StudyRepository studyRepository;
    private final MemberRepository memberRepository;
    private final StudyCommentQuerydslRepository studyCommentQuerydslRepository;

    public Long createStudyComment(Long studyId, StudyCommentRequest commentRequest) {
        Study study = getStudyById(studyId);
        Long accessMemberId = SecurityContextHolderUtil.getMemberId();
        Member member = getMemberById(accessMemberId);

        StudyComment studyComment =
            studyCommentMapper.toStudyComment(study, member, commentRequest);
        return studyCommentRepository.save(studyComment).getId();
    }

    public Slice<StudyCommentResponse> getAllStudyComment(Long studyId, Long lastId, Pageable pageable) {
        List<StudyCommentResponse> results =
            studyCommentQuerydslRepository.searchBySlice(studyId, lastId, pageable);
        return checkLastPage(pageable, results);
    }

    private Slice<StudyCommentResponse> checkLastPage(Pageable pageable, List<StudyCommentResponse> results) {

        boolean hasNext = false;

        // 조회한 결과 개수가 요청한 페이지 사이즈보다 크면 뒤에 더 있음, next = true
        if (results.size() > pageable.getPageSize()) {
            hasNext = true;
            results.remove(pageable.getPageSize());
        }

        return new SliceImpl<>(results, pageable, hasNext);
    }

    public void updateComment(Long studyId, Long commentId, StudyCommentRequest commentRequest) {
        getStudyById(studyId);
        Long accessMemberId = SecurityContextHolderUtil.getMemberId();
        getMemberById(accessMemberId);
        StudyComment comment = getStudyComment(commentId);

        if (!comment.getMember().getId().equals(accessMemberId)) {
            throw new MyCommunityForbiddenException(403, "내가 작성하지 않은 글은 수정할 수 없습니다.");
        }

        comment.updateComment(commentRequest.getContent());
        studyCommentRepository.save(comment);
    }

    public void deleteComment(Long studyId, Long commentId) {
        getStudyById(studyId);
        Long accessMemberId = SecurityContextHolderUtil.getMemberId();
        getMemberById(accessMemberId);
        StudyComment comment = getStudyComment(commentId);

        if (!comment.getMember().getId().equals(accessMemberId)) {
            throw new MyCommunityForbiddenException(403, "내가 작성하지 않은 글은 삭제할 수 없습니다.");
        }

        studyCommentRepository.deleteById(commentId);
    }

    private StudyComment getStudyComment(Long commentId) {
        return studyCommentRepository.findById(commentId).orElseThrow(() -> new DataNotFoundException());
    }

    private Study getStudyById(Long studyId) {
        return studyRepository.findById(studyId).orElseThrow(() -> new DataNotFoundException());
    }

    private Member getMemberById(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(() -> new DataNotFoundException());
    }
}