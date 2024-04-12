package org.devridge.api.infrastructure.community.freeboard;

import java.util.List;
import org.devridge.api.domain.community.entity.CommunityComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CommunityCommentRepository extends JpaRepository<CommunityComment, Long> {

    List<CommunityComment> findByCommunityId(Long communityId);

    @Modifying
    @Query(
        value = "UPDATE CommunityComment " +
                "SET likes = :likes, dislikes = :dislikes " +
                "WHERE id = :id")
    void updateLikeDislike(@Param("likes") Long likes, @Param("dislikes") Long dislikes, @Param("id") Long id);
}
