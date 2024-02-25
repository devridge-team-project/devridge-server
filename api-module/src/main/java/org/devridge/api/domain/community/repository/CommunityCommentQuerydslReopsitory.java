package org.devridge.api.domain.community.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.devridge.api.domain.community.dto.response.CommunityCommentResponse;
import org.devridge.api.domain.community.dto.response.MemberInfoResponse;
import org.devridge.api.domain.community.entity.CommunityComment;
import org.devridge.api.domain.community.entity.QCommunityComment;
import org.devridge.api.domain.member.entity.Member;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CommunityCommentQuerydslReopsitory {

    private final JPAQueryFactory jpaQueryFactory;
    private QCommunityComment comment = QCommunityComment.communityComment;

    public Slice<CommunityCommentResponse> searchBySlice(Long communityId, Long lastId, Pageable pageable) {
        List<CommunityComment> lists = jpaQueryFactory
            .select(comment)
            .from(comment)
            .leftJoin(comment.member)
            .where(
                ltId(lastId),
                comment.isDeleted.eq(false),
                comment.community.id.eq(communityId)
            )
            .orderBy(comment.id.desc())
            .limit(pageable.getPageSize() + 1)
            .fetch();

        List<CommunityCommentResponse> results = new ArrayList<>();


        for (CommunityComment comment : lists) {
            MemberInfoResponse memberInfoResponse;
            try {
                Member member = comment.getMember();
                 memberInfoResponse = MemberInfoResponse.builder()
                        .memberId(member.getId())
                        .nickName(member.getNickname())
                        .profileImageUrl(member.getProfileImageUrl())
                        .introduction(member.getIntroduction())
                        .build();
            } catch (EntityNotFoundException e) {
                 memberInfoResponse = MemberInfoResponse.builder()
                        .memberId(0L)
                        .nickName("기본 닉네임")
                        .profileImageUrl("기본 프로필 이미지 URL")
                        .introduction("기본 소개")
                        .build();
            }

            results.add(
                CommunityCommentResponse.builder()
                    .commentId(comment.getId())
                    .content(comment.getContent())
                    .createdAt(comment.getCreatedAt())
                    .updatedAt(comment.getUpdatedAt())
                    .likeCount(comment.getLikeCount())
                    .dislikeCount(comment.getDislikeCount())
                    .memberInfoResponse(memberInfoResponse)
                    .build()
            );
        }
        return checkLastPage(pageable, results);
    }

    // no-offset 방식 처리하는 메서드
    private BooleanExpression ltId(Long communityId) {
        if (communityId == null) {
            return null;
        }

        return comment.id.lt(communityId);
    }

    private Slice<CommunityCommentResponse> checkLastPage(Pageable pageable, List<CommunityCommentResponse> results) {

        boolean hasNext = false;

        // 조회한 결과 개수가 요청한 페이지 사이즈보다 크면 뒤에 더 있음, next = true
        if (results.size() > pageable.getPageSize()) {
            hasNext = true;
            results.remove(pageable.getPageSize());
        }

        return new SliceImpl<>(results, pageable, hasNext);
    }

}
