package org.devridge.api.infrastructure.community.freeboard;

import org.devridge.api.domain.community.entity.Community;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CommunityRepository extends JpaRepository<Community, Long> {

    @Modifying
    @Query(
        value = "UPDATE Community c " +
                "SET c.views = c.views + 1 " +
                "WHERE c.id = :id")
    void updateView(@Param("id") Long id);

    @Modifying
    @Query(
        value = "UPDATE Community " +
                "SET likes = :likes, dislikes = :dislikes " +
                "WHERE id = :id")
    void updateLikeDislike(
        @Param("likes") Long likes,
        @Param("dislikes") Long dislikes,
        @Param("id") Long id);
}
