package org.devridge.api.infrastructure.member;

import org.devridge.api.domain.member.entity.Occupation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OccupationRepository extends JpaRepository<Occupation, Long> {
}
