package org.devridge.api.infrastructure.community.freeboard;

import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.devridge.api.domain.community.dto.response.CommunitySliceResponse;
import org.devridge.api.domain.community.dto.response.UserInformation;
import org.devridge.api.domain.community.entity.CommunityHashtag;
import org.devridge.api.domain.community.entity.QCommunity;
import org.devridge.api.domain.community.entity.QCommunityHashtag;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class CommunityQuerydslRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private QCommunity community = QCommunity.community;
    private QCommunityHashtag communityHashtag = QCommunityHashtag.communityHashtag;

    public List<CommunityHashtag> findCommunityHashtagsInCommunityIds(List<Long> communityIds) {
        return jpaQueryFactory
            .selectFrom(communityHashtag)
            .leftJoin(communityHashtag.hashtag).fetchJoin()
            .where(communityHashtag.community.id.in(communityIds))
            .fetch();
    }

    public List<CommunitySliceResponse> searchByCommunity(Long lastId, Pageable pageable) {
        return jpaQueryFactory
            .selectFrom(community)
            .leftJoin(community.member)
            .where(
                ltId(lastId),
                community.isDeleted.eq(false)
            )
            .orderBy(community.id.desc())
            .limit(pageable.getPageSize() + 1)
            .transform(GroupBy.groupBy(community.id).list(
                    Projections.fields(
                        CommunitySliceResponse.class,
                        community.id,
                        community.title,
                        community.content,
                        community.viewCount,
                        community.likeCount,
                        community.comments.size().longValue().as("comments"),
                        Projections.constructor(
                            UserInformation.class,
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
                        ).as("member"),
                        community.createdAt,
                        community.updatedAt,
                        community.scraps.size().longValue().as("scraps")
                    )
                )
            );
    }

    // no-offset 방식 처리하는 메서드
    private BooleanExpression ltId(Long communityId) {
        if (communityId == null) {
            return null;
        }

        return community.id.lt(communityId);
    }

}
