package org.devridge.api.infrastructure.community.project;

import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.devridge.api.domain.community.entity.Project;
import org.devridge.api.domain.community.entity.QProject;
import org.devridge.api.domain.skill.entity.ProjectSkill;
import org.devridge.api.domain.skill.entity.QProjectSkill;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class ProjectQuerydslRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private QProject project = QProject.project;
    private QProjectSkill projectSkill = QProjectSkill.projectSkill;

    public List<ProjectSkill> findProjectSkillsInProjectIds(List<Long> projectIds) {
        return jpaQueryFactory
            .selectFrom(projectSkill)
            .leftJoin(projectSkill.skill).fetchJoin()
            .where(projectSkill.id.projectId.in(projectIds))
            .fetch();
    }

    public List<Project> searchByProject(Long lastId, Pageable pageable) {
        return jpaQueryFactory
            .selectFrom(project)
            .leftJoin(project.member)
            .where(
                ltId(lastId),
                project.isDeleted.eq(false)
            )
            .orderBy(project.id.desc())
            .limit(pageable.getPageSize() + 1)
            .transform(GroupBy.groupBy(project.id)
                .list(project)
            );
    }

    // no-offset 방식 처리하는 메서드
    private BooleanExpression ltId(Long projectId) {
        if (projectId == null) {
            return null;
        }

        return project.id.lt(projectId);
    }
}
