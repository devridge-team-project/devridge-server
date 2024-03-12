package org.devridge.api.infrastructure.community;

import org.devridge.api.domain.community.entity.CommunityScrap;
import org.devridge.api.domain.community.entity.id.CommunityScrapId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CommunityScrapRepository extends JpaRepository<CommunityScrap, CommunityScrapId> {

    @Modifying
    @Query(
        value = "UPDATE CommunityScrap cs " +
                "SET cs.isDeleted = false " +
                "WHERE cs.id = :id"
    )
    void restoreById(@Param("id") CommunityScrapId id);
}
