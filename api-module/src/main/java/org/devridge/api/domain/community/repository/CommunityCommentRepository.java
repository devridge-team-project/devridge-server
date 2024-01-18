package org.devridge.api.domain.community.repository;

import java.util.List;
import org.devridge.api.domain.community.entity.CommunityComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommunityCommentRepository extends JpaRepository<CommunityComment, Long> {

    List<CommunityComment> findByCommunityId(Long communityId);
}
