package org.devridge.api.domain.qna.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.core.types.Projections;

import lombok.RequiredArgsConstructor;

import org.devridge.api.domain.qna.dto.response.FindWriterInformation;
import org.devridge.api.domain.qna.dto.response.GetAllCommentByQnAId;
import org.devridge.api.domain.qna.dto.response.GetAllQnAResponse;
import org.devridge.api.domain.qna.dto.response.QFindWriterInformation;
import org.devridge.api.domain.qna.entity.QQnA;

import org.devridge.api.domain.qna.entity.QQnAComment;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class QnAQuerydslRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final int PAGE_SIZE = 10;

    private QQnA qQnA = QQnA.qnA;
    private QQnAComment qQnAComment = QQnAComment.qnAComment;

    /**
     * 조회수 기준 상위 5개 반환, 리스트 반환이므로 id, 제목, 답변수, 추천수, 조회수만 반환
     * 답변 수는 comment길이, 추천수 트리거, 조회수 트리거
     */
    public List<GetAllQnAResponse> findAllQnASortByViews() {
        return jpaQueryFactory
            .select(
                Projections.constructor(
                    GetAllQnAResponse.class,
                    qQnA.id,
                    qQnA.title,
                    qQnA.likes,
                    qQnA.views,
                    qQnA.comments.size()
                )
            )
            .from(qQnA)
            .orderBy(qQnA.views.desc())
            .limit(5)
            .fetch();
    }

    /**
     * 최신순 무한 스크롤
     */
    public List<GetAllQnAResponse> findAllQnASortByLatest(Long lastIndex) {
        return jpaQueryFactory
            .select(
                Projections.constructor(
                    GetAllQnAResponse.class,
                    qQnA.id,
                    qQnA.title,
                    qQnA.likes,
                    qQnA.views,
                    qQnA.comments.size(),
                    qQnA.createdAt
                )
            )
            .from(qQnA)
            .where(qQnA.id.loe(lastIndex))
            .orderBy(qQnA.id.desc())
            .limit(PAGE_SIZE)
            .fetch();
    }

    public List<GetAllCommentByQnAId> findAllQnAComment(Long lastIndex, Long qnaId) {
        return jpaQueryFactory
            .select(
                Projections.constructor(
                    GetAllCommentByQnAId.class,
                    qQnAComment.id,
                    new QFindWriterInformation(
                        qQnAComment.member.id,
                        qQnAComment.member.nickname,
                        qQnAComment.member.profileImageUrl,
                        qQnAComment.member.introduction
                    ),
                    qQnAComment.content,
                    qQnAComment.likes,
                    qQnAComment.dislikes,
                    qQnAComment.createdAt
                )
            )
            .from(qQnAComment)
            .where(qQnAComment.id.loe(lastIndex), qQnAComment.qna.id.eq(qnaId))
            .orderBy(qQnAComment.id.desc())
            .limit(PAGE_SIZE)
            .fetch();
    }
}
