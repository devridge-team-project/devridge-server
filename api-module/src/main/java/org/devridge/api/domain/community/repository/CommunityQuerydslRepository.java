package org.devridge.api.domain.community.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.devridge.api.domain.community.dto.response.CommunityListResponse;
import org.devridge.api.domain.community.entity.QCommunity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class CommunityQuerydslRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private QCommunity community = QCommunity.community;

    public Slice<CommunityListResponse> searchBySlice(Long lastId,Pageable pageable) {
        List<CommunityListResponse> results = jpaQueryFactory
            .select(
                Projections.constructor(
                    CommunityListResponse.class,
                    community.id,
                    community.title,
                    community.views,
                    community.likeCount,
                    community.comments.size().longValue()
                )
            )
            .from(community)
            .where(
                ltId(lastId),
                community.isDeleted.eq(false)
            )
            .orderBy(community.id.desc())
            .limit(pageable.getPageSize() + 1)
            .fetch();

        return checkLastPage(pageable, results);
    }

    // no-offset 방식 처리하는 메서드
    private BooleanExpression ltId(Long communityId) {
        if (communityId == null) {
            return null;
        }

        return community.id.lt(communityId);
    }

    private Slice<CommunityListResponse> checkLastPage(Pageable pageable, List<CommunityListResponse> results) {

        boolean hasNext = false;

        // 조회한 결과 개수가 요청한 페이지 사이즈보다 크면 뒤에 더 있음, next = true
        if (results.size() > pageable.getPageSize()) {
            hasNext = true;
            results.remove(pageable.getPageSize());
        }

        return new SliceImpl<>(results, pageable, hasNext);
    }
}
