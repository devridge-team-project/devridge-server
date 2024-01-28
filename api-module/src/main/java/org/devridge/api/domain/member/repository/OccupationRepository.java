package org.devridge.api.domain.member.repository;

import org.devridge.api.domain.member.entity.Occupation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OccupationRepository extends JpaRepository<Occupation, Long> {
}
