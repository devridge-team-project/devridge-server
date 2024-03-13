package org.devridge.api.infrastructure.community.study;

import org.devridge.api.domain.community.entity.Study;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StudyRepository extends JpaRepository<Study, Long> {

    @Modifying
    @Query(
        value = "UPDATE Study s " +
                "SET s.views = s.views + 1 " +
                "WHERE s.id = :id")
    void updateView(@Param("id") Long id);
}
