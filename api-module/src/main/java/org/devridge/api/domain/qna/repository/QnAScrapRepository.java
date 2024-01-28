package org.devridge.api.domain.qna.repository;

import org.devridge.api.domain.qna.entity.QnAScrap;
import org.devridge.api.domain.qna.entity.id.QnAScrapId;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface QnAScrapRepository extends JpaRepository<QnAScrap, QnAScrapId> {

    @Modifying
    @Query(
        nativeQuery = true,
        value = "INSERT INTO qna_scrap (member_id, qna_id) " +
                "VALUES (:memberId, :qnaId) " +
                "ON DUPLICATE KEY UPDATE is_deleted = IF(is_deleted, 0, 1)"
    )
    void createOrUpdateQnAScrap(@Param("memberId") Long memberId, @Param("qnaId") Long qnaId);
}
