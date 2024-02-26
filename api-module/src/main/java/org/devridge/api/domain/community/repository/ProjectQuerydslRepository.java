package org.devridge.api.domain.community.repository;

import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.devridge.api.domain.community.dto.response.ProjectListResponse;
import org.devridge.api.domain.community.entity.QProject;
import org.devridge.api.domain.skill.entity.QProjectSkill;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class ProjectQuerydslRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private QProject project = QProject.project;
    private QProjectSkill projectSkill = QProjectSkill.projectSkill;


    public Slice<ProjectListResponse> searchBySlice(Long lastId, Pageable pageable) {
        List<ProjectListResponse> results = jpaQueryFactory
            .selectFrom(project)
            .leftJoin(project.member)
            .leftJoin(project.projectSkills, projectSkill)
            .where(
                ltId(lastId),
                project.isDeleted.eq(false)
            )
            .orderBy(project.id.desc())
            .limit(pageable.getPageSize() + 1)
            .transform(GroupBy.groupBy(project.id).
                list(Projections.constructor(
                    ProjectListResponse.class,
                    project.id,
                    project.category,
                    project.title,
                    project.content,
                    project.likes,
                    project.dislikes,
                    project.views,
                    project.isRecruiting,
                    GroupBy.list(Projections.constructor(String.class, projectSkill.skill.skill)),
                    project.meeting
            )));


        return checkLastPage(pageable, results);
    }

    // no-offset 방식 처리하는 메서드
    private BooleanExpression ltId(Long communityId) {
        if (communityId == null) {
            return null;
        }

        return project.id.lt(communityId);
    }

    private Slice<ProjectListResponse> checkLastPage(Pageable pageable, List<ProjectListResponse> results) {

        boolean hasNext = false;

        // 조회한 결과 개수가 요청한 페이지 사이즈보다 크면 뒤에 더 있음, next = true
        if (results.size() > pageable.getPageSize()) {
            hasNext = true;
            results.remove(pageable.getPageSize());
        }

        return new SliceImpl<>(results, pageable, hasNext);
    }
}
