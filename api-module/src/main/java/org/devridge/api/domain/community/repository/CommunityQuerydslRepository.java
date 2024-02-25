package org.devridge.api.domain.community.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.devridge.api.domain.community.dto.response.CommunitySliceResponse;
import org.devridge.api.domain.community.dto.response.HashtagResponse;
import org.devridge.api.domain.community.dto.response.MemberInfoResponse;
import org.devridge.api.domain.community.entity.Community;
import org.devridge.api.domain.community.entity.QCommunity;
import org.devridge.api.domain.community.entity.QCommunityHashtag;
import org.devridge.api.domain.member.entity.Member;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class CommunityQuerydslRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private QCommunity community = QCommunity.community;
    private QCommunityHashtag communityHashtag = QCommunityHashtag.communityHashtag;


    public Slice<CommunitySliceResponse> searchBySlice(Long lastId, Pageable pageable) {
        List<Community> tuples = jpaQueryFactory
            .select(community)
            .from(community)
            .leftJoin(community.member)
            .where(
                ltId(lastId),
                community.isDeleted.eq(false)
            )
            .orderBy(community.id.desc())
            .limit(pageable.getPageSize() + 1)
            .fetch();

        List<CommunitySliceResponse> results = new ArrayList<>();

        for (Community community : tuples) {
            MemberInfoResponse memberInfoResponse;
            try {
                Member member = community.getMember();
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

            List<HashtagResponse> hashtags = jpaQueryFactory
                .select(
                    Projections.constructor(
                        HashtagResponse.class,
                        communityHashtag.hashtag.id,
                        communityHashtag.hashtag.word,
                        communityHashtag.hashtag.count
                    )
                )
                .from(communityHashtag)
                .where(communityHashtag.community.id.eq(community.getId()))
                .fetch();

            results.add(
                CommunitySliceResponse.builder()
                    .id(community.getId())
                    .title(community.getTitle())
                    .views(community.getViews())
                    .likeCount(community.getLikeCount())
                    .comments(Long.valueOf(community.getComments().size()))
                    .member(memberInfoResponse)
                    .createdAt(community.getCreatedAt())
                    .updatedAt(community.getUpdatedAt())
                    .hashtags(hashtags)
                    .scraps(Long.valueOf(community.getScraps().size()))
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

        return community.id.lt(communityId);
    }

    private Slice<CommunitySliceResponse> checkLastPage(Pageable pageable, List<CommunitySliceResponse> results) {

        boolean hasNext = false;

        // 조회한 결과 개수가 요청한 페이지 사이즈보다 크면 뒤에 더 있음, next = true
        if (results.size() > pageable.getPageSize()) {
            hasNext = true;
            results.remove(pageable.getPageSize());
        }

        return new SliceImpl<>(results, pageable, hasNext);
    }
}
