package org.devridge.api.infrastructure.community.study;

import org.devridge.api.domain.community.entity.StudyScrap;
import org.devridge.api.domain.community.entity.id.StudyScrapId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StudyScrapRepository extends JpaRepository<StudyScrap, StudyScrapId> {

    @Modifying
    @Query(
        value = "UPDATE StudyScrap s " +
            "SET s.isDeleted = false " +
            "WHERE s.id = :id"
    )
    void restoreById(@Param("id") StudyScrapId id);
}