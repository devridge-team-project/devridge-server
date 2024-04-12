package org.devridge.api.infrastructure.community.study;

import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.devridge.api.common.dto.UserInformation;
import org.devridge.api.domain.community.dto.response.StudyCommentResponse;
import org.devridge.api.domain.community.entity.QStudyComment;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class StudyCommentQuerydslRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private QStudyComment comment = QStudyComment.studyComment;

    public List<StudyCommentResponse> searchBySlice(Long studyId, Long lastId, Pageable pageable) {
        return jpaQueryFactory
            .selectFrom(comment)
            .leftJoin(comment.member)
            .where(
                ltId(lastId),
                comment.isDeleted.eq(false),
                comment.study.id.eq(studyId)
            )
            .orderBy(comment.id.desc())
            .limit(pageable.getPageSize() + 1)
            .transform(GroupBy.groupBy(comment.id)
                .list(Projections.constructor(
                        StudyCommentResponse.class,
                        comment.id,
                        comment.content,
                        comment.createdAt,
                        comment.updatedAt,
                        comment.likes,
                        comment.dislikes,

                        Projections.constructor(
                            UserInformation.class,
                            Expressions.cases()
                                .when(comment.member.nickname.isNull())
                                .then(0L)
                                .otherwise(comment.member.id),
                            Expressions.cases()
                                .when(comment.member.nickname.isNull())
                                .then("DefaultNickname")
                                .otherwise(comment.member.nickname),
                            Expressions.cases()
                                .when(comment.member.nickname.isNull())
                                .then("DefaultUrl")
                                .otherwise(comment.member.profileImageUrl),
                            Expressions.cases()
                                .when(comment.member.nickname.isNull())
                                .then("DefaultIntroduction")
                                .otherwise(comment.member.introduction)
                        )
                    )
                ));
    }

    // no-offset 방식 처리하는 메서드
    private BooleanExpression ltId(Long studyId) {
        if (studyId == null) {
            return null;
        }

        return comment.id.lt(studyId);
    }
}
