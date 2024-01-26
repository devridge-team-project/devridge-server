package org.devridge.api.domain.community.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.devridge.api.domain.community.entity.LikeStatus;
import org.devridge.api.domain.community.entity.QCommunityComment;
import org.devridge.api.domain.community.entity.QCommunityCommentLikeDislike;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class CommunityQuerydslRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private QCommunityComment qCommunityComment = QCommunityComment.communityComment;
    private QCommunityCommentLikeDislike qCommunityCommentLikeDislike = QCommunityCommentLikeDislike.communityCommentLikeDislike;

    @Transactional
    public void updateLikeCountByCommentId(Long commentId) {
        long likeCount = jpaQueryFactory
            .select(qCommunityCommentLikeDislike.communityComment.id.count())
            .from(qCommunityCommentLikeDislike)
            .where(qCommunityCommentLikeDislike.communityComment.id.eq(commentId)
                .and(qCommunityCommentLikeDislike.status.eq(LikeStatus.G)))
            .fetchOne();

        long dislikeCount = jpaQueryFactory
            .select(qCommunityCommentLikeDislike.communityComment.id.count())
            .from(qCommunityCommentLikeDislike)
            .where(qCommunityCommentLikeDislike.communityComment.id.eq(commentId)
                .and(qCommunityCommentLikeDislike.status.eq(LikeStatus.B)))
            .fetchOne();

        jpaQueryFactory
            .update(qCommunityComment)
            .set(qCommunityComment.likeCount, likeCount - dislikeCount)
            .where(qCommunityComment.id.eq(commentId))
            .execute();
    }
}
