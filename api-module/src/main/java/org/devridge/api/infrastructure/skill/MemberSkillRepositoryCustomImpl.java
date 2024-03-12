package org.devridge.api.infrastructure.skill;

import org.devridge.api.domain.skill.entity.MemberSkill;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

public class MemberSkillRepositoryCustomImpl implements MemberSkillRepositoryCustom {

    private final JdbcTemplate jdbcTemplate;

    public MemberSkillRepositoryCustomImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    @Transactional
    public void bulkInsert(List<MemberSkill> memberSkills) {
        String sql = "INSERT INTO member_skill (member_id, skill_id) VALUES (?, ?)";
        List<Object[]> batchArgs = new ArrayList<>();

        for (MemberSkill memberSkill : memberSkills) {
            Object[] values = {
                    memberSkill.getId().getMemberId(),
                    memberSkill.getId().getSkillId()
            };
            batchArgs.add(values);
        }

        jdbcTemplate.batchUpdate(sql, batchArgs);
    }
}
