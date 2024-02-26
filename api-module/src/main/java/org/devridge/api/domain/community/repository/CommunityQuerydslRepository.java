package org.devridge.api.domain.community.repository;

import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.devridge.api.domain.community.dto.response.CommunitySliceResponse;
import org.devridge.api.domain.community.dto.response.HashtagResponse;
import org.devridge.api.domain.community.dto.response.MemberInfoResponse;
import org.devridge.api.domain.community.entity.QCommunity;
import org.devridge.api.domain.community.entity.QCommunityHashtag;
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
        List<CommunitySliceResponse> results = jpaQueryFactory
            .selectFrom(community)
            .leftJoin(community.member)
            .leftJoin(community.hashtags, communityHashtag)
            .leftJoin(communityHashtag.hashtag)
            .where(
                ltId(lastId),
                community.isDeleted.eq(false)
            )
            .orderBy(community.id.desc())
            .limit(pageable.getPageSize() + 1)
            .transform(GroupBy.groupBy(community.id)
                .list(Projections.constructor(
                    CommunitySliceResponse.class,
                    community.id,
                    community.title,
                    community.views,
                    community.likeCount,
                    community.comments.size().longValue(),

                    Projections.constructor(
                        MemberInfoResponse.class,
                        Expressions.cases()
                            .when(community.member.nickname.isNull())
                            .then(0L)
                            .otherwise(community.member.id),
                        Expressions.cases()
                            .when(community.member.nickname.isNull())
                            .then("DefaultNickname")
                            .otherwise(community.member.nickname),
                        Expressions.cases()
                            .when(community.member.nickname.isNull())
                            .then("DefaultUrl")
                            .otherwise(community.member.profileImageUrl),
                        Expressions.cases()
                            .when(community.member.nickname.isNull())
                            .then("DefaultIntroduction")
                            .otherwise(community.member.introduction)
                    ),

                    community.createdAt,
                    community.updatedAt,

                    GroupBy.list(Projections.fields(
                        HashtagResponse.class,
                        communityHashtag.hashtag.id,
                        communityHashtag.hashtag.word,
                        communityHashtag.hashtag.count
                        )),

                    community.scraps.size().longValue()
                ))
            );
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
