package org.devridge.api.domain.qna.repository;

import org.devridge.api.domain.qna.entity.QnAComment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface QnACommentRepository extends JpaRepository<QnAComment, Long> {

    @Modifying(clearAutomatically = true)
    @Query(
        value = "UPDATE QnAComment " +
                "SET content = :content " +
                "WHERE id = :commentId"
    )
    void updateQnAComment(Long commentId, String content);
}