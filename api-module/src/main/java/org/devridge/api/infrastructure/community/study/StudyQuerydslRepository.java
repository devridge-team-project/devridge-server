package org.devridge.api.infrastructure.community.study;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.devridge.api.domain.community.entity.QStudy;
import org.devridge.api.domain.community.entity.Study;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class StudyQuerydslRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private QStudy study = QStudy.study;

    public List<Study> searchByStudy(Long lastId, Pageable pageable) {
        return jpaQueryFactory
            .select(study)
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
