package org.devridge.api.infrastructure.community.study;

import org.devridge.api.domain.community.entity.StudyComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StudyCommentRepository extends JpaRepository<StudyComment, Long> {

    @Modifying
    @Query(
        value = "UPDATE StudyComment " +
                "SET likes = :likes, dislikes = :dislikes " +
                "WHERE id = :id")
    void updateLikeDislike(@Param("likes") Long likes, @Param("dislikes") Long dislikes, @Param("id") Long id);

}
