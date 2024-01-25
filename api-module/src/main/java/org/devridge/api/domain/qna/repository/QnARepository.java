package org.devridge.api.domain.qna.repository;

import org.devridge.api.domain.qna.entity.QnA;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface QnARepository extends JpaRepository<QnA, Long> {

    @Modifying(clearAutomatically = true)
    @Query(
        value = "UPDATE QnA " +
                "SET title = :title, content = :content " +
                "WHERE id = :qnaId"
    )
    void updateQnA(@Param("title") String title, @Param("content") String content, @Param("qnaId") Long qnaId);

    @Modifying(clearAutomatically = true)
    @Query(
        value = "UPDATE QnA " +
                "SET likes = :likes, dislikes = :dislikes " +
                "WHERE id = :qnaId"
    )
    void updateLikeAndDiscount(@Param("likes") int likes, @Param("dislikes") int dislikes, @Param("qnaId") Long qnaId);
}
