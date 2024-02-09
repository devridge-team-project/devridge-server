package org.devridge.api.domain.community.repository;

import java.util.List;
import java.util.Optional;
import org.devridge.api.domain.community.entity.CommunityHashtag;
import org.devridge.api.domain.community.entity.id.CommunityHashtagId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommunityHashtagRepository extends JpaRepository<CommunityHashtag, CommunityHashtagId> {

    void deleteByCommunityId(Long communityId);

    List<CommunityHashtag> findAllByCommunityId(Long communityId);

    @Modifying
    @Query(
        nativeQuery = true,
        value = "UPDATE community_hashtag " +
                "SET is_deleted = false " +
                "WHERE community_id = :communityId AND hashtag_id = :hashtagId"
    )
    void restoreByCommunityIdAndHashtagId(@Param("communityId") Long communityId, @Param("hashtagId")Long hashtagId);

    @Query(
        nativeQuery = true,
        value = "SELECT * " +
                "FROM community_hashtag " +
                "WHERE community_id = :CommunityId AND hashtag_id = :HashtagId"
    )
    Optional<CommunityHashtag> findByCommunityIdAndHashtagId(@Param("CommunityId") Long CommunityId, @Param("HashtagId")Long HashtagId);

    List<CommunityHashtag> findAllByHashtagId(Long hashtagId);
}
