package org.devridge.api.domain.qna.repository;

import org.devridge.api.domain.qna.entity.QnAComment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface QnACommentRepository extends JpaRepository<QnAComment, Long> {

    @Modifying(clearAutomatically = true)
    @Query(
        value = "UPDATE QnAComment " +
                "SET content = :content " +
                "WHERE id = :commentId"
    )
    void updateQnAComment(@Param("content") String content, @Param("commentId") Long commentId);

    @Modifying(clearAutomatically = true)
    @Query(
        value = "UPDATE QnAComment " +
                "SET likes = :likes, dislikes = :dislikes " +
                "WHERE id = :qnaCommentId"
    )
    void updateLikeAndDiscount(
        @Param("likes") int likes,
        @Param("dislikes") int dislikes,
        @Param("qnaCommentId") Long qnaCommentId
    );
}
