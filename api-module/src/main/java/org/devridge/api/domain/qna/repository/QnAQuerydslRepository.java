package org.devridge.api.domain.qna.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.core.types.Projections;

import lombok.RequiredArgsConstructor;
import org.devridge.api.domain.qna.dto.response.GetAllQnAResponse;
import org.devridge.api.domain.qna.entity.QQnA;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class QnAQuerydslRepository {

    private final JPAQueryFactory jpaQueryFactory;

    private QQnA qQnA = QQnA.qnA;

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
     * TODO: 추후 무한 스크롤 변경 예정
     */
    public List<GetAllQnAResponse> findAllQnASortByLatest() {
        return jpaQueryFactory
            .select(
                Projections.constructor(
                    GetAllQnAResponse.class,
                    qQnA.id,
                    qQnA.title,
                    qQnA.views,
                    qQnA.comments.size()
                )
            )
            .from(qQnA)
            .orderBy(qQnA.createdAt.desc())
            .fetch();
    }
}
