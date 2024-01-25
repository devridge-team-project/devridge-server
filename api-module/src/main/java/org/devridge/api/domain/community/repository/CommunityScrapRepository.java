package org.devridge.api.domain.community.repository;

import javax.transaction.Transactional;
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
    @Transactional
    @Query("UPDATE CommunityScrap cs "
        + "SET cs.isDeleted = false "
        + "WHERE cs.community.id = :communityId "
        + "AND cs.member.id = :memberId")
    void reCreateScrap(@Param("communityId") Long communityId, @Param("memberId") Long memberId);

    @Query(value = "SELECT COUNT(*) FROM community_scrap " +
        "WHERE community_id = :communityId " +
        "AND member_id = :memberId ", nativeQuery = true)
    Long checkSoftDelete(@Param("communityId") Long communityId, @Param("memberId") Long memberId);
}
