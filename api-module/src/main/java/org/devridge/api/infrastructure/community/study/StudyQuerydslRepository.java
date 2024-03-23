package org.devridge.api.infrastructure.community.study;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.devridge.api.domain.community.dto.response.StudyListResponse;
import org.devridge.api.domain.community.entity.QStudy;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class StudyQuerydslRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private QStudy study = QStudy.study;

    public List<StudyListResponse> searchByStudy(Long lastId, Pageable pageable) {
        return jpaQueryFactory
            .select(Projections.fields(StudyListResponse.class,
                study.id.as("studyId"),
                study.category,
                study.title,
                study.content,
                study.images,
                study.likes,
                study.dislikes,
                study.views,
                study.location,
                study.totalPeople,
                study.currentPeople
            ))
            .from(study)
            .where(ltId(lastId))
            .orderBy(study.id.desc())
            .limit(pageable.getPageSize() + 1)
            .fetch();

    }

    private BooleanExpression ltId(Long studyId) {
        if (studyId == null) {
            return null;
        }

        return study.id.lt(studyId);
    }
}
