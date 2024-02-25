package org.devridge.api.domain.community.repository;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.devridge.api.domain.community.dto.response.CommunitySliceResponse;
import org.devridge.api.domain.community.dto.response.HashtagResponse;
import org.devridge.api.domain.community.dto.response.MemberInfoResponse;
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
        List<Tuple> tuples = jpaQueryFactory
            .select(
                community.id,
                community.title,
                community.views,
                community.likeCount,
                community.comments.size().longValue(),

                community.member,

                community.member.id,
                community.member.nickname,
                community.member.profileImageUrl,
                community.member.introduction,

                community.createdAt,
                community.updatedAt
            )
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


        for (Tuple tuple : tuples) {
            Long communityId = tuple.get(community.id);
            String title = tuple.get(community.title);
            Long views = tuple.get(community.views);
            Long likeCount = tuple.get(community.likeCount);
            Long comments = tuple.get(community.comments.size().longValue());

            Long memberId = tuple.get(community.member.id);
            String nickname = tuple.get(community.member.nickname);
            String profileImageUrl = tuple.get(community.member.profileImageUrl);
            String introduction = tuple.get(community.member.introduction);

            if (nickname == null) {
                memberId = 0L;
                nickname = "기본 닉네임";
                profileImageUrl = "기본 프로필 이미지 URL";
                introduction = "기본 소개";
            }

            Member member = tuple.get(community.member);

            MemberInfoResponse memberInfoResponse = MemberInfoResponse.builder()
                .memberId(memberId)
                .nickName(nickname)
                .profileImageUrl(profileImageUrl)
                .introduction(introduction)
                .build();

            LocalDateTime createdAt = tuple.get(community.createdAt);
            LocalDateTime updatedAt = tuple.get(community.updatedAt);

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
                .where(communityHashtag.community.id.eq(tuple.get(community.id)))
                .fetch();

            results.add(
                CommunitySliceResponse.builder()
                    .id(communityId)
                    .title(title)
                    .views(views)
                    .likeCount(likeCount)
                    .comments(comments)
                    .member(memberInfoResponse)
                    .createdAt(createdAt)
                    .updatedAt(updatedAt)
                    .hashtags(hashtags)
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
