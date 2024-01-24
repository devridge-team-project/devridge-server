package org.devridge.api.domain.skill.repository;

import org.devridge.api.domain.skill.entity.MemberSkill;
import org.devridge.api.domain.skill.entity.key.MemberSkillId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MemberSkillRepository extends JpaRepository<MemberSkill, MemberSkillId> {

    @Query("select memberSkill.id.skillId FROM MemberSkill memberSkill " +
            "WHERE memberSkill.id.memberId = :memberId")
    List<Long> findSkillIdByMemberId(@Param("memberId") Long memberId);
}
