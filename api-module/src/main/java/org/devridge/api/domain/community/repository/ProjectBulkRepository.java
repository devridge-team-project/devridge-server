package org.devridge.api.domain.community.repository;

import java.sql.PreparedStatement;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.devridge.api.domain.community.entity.Project;
import org.devridge.api.domain.skill.entity.Skill;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class ProjectBulkRepository {

    private final JdbcTemplate jdbcTemplate;

    @Transactional
    public void saveAll(List<Skill> skills, Project project){
        String sql = "INSERT INTO project_skill (project_id, skill_id)"+
            "VALUES (?, ?)";
        Long projectId = project.getId();

        jdbcTemplate.batchUpdate(sql,
            skills,
            skills.size(),
            (PreparedStatement ps, Skill skill) -> {
                ps.setLong(1, projectId);
                ps.setLong(2, skill.getId());
            });
    }
}
