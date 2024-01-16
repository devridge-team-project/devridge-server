package org.devridge.api.domain.community;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CommunityRepository extends JpaRepository<Community, Long> {

    @Modifying
    @Query("update Community q set q.views = q.views + 1 where q.id = :id")
    void updateView(@Param("id") Long id);
}
