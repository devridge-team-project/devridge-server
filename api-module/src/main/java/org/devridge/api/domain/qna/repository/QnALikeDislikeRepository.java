package org.devridge.api.domain.qna.repository;

import org.devridge.api.domain.member.entity.Member;
import org.devridge.api.domain.qna.dto.type.LikeStatus;
import org.devridge.api.domain.qna.entity.QnA;
import org.devridge.api.domain.qna.entity.QnALikeDislike;
import org.devridge.api.domain.qna.entity.id.QnALikeDislikeId;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface QnALikeDislikeRepository extends JpaRepository<QnALikeDislike, QnALikeDislikeId> {

    @Modifying
    @Query(
            value = "UPDATE QnALikeDislike " +
                    "SET status = :status, isDeleted = :isDeleted " +
                    "WHERE id.member = :member AND id.qna = :qna"
    )
    void updateQnALikeStatusToGoodOrBad(
        @Param("member")Member member,
        @Param("qna") QnA qna,
        @Param("isDeleted") Boolean isDeleted,
        @Param("status")LikeStatus status
    );

    @Query(
        value = "SELECT COUNT(q) " +
                "FROM QnALikeDislike q " +
                "WHERE q.id.qna = :qna AND q.status = 'G' AND q.isDeleted = false"
    )
    int countQnALikeByQnAId(@Param("qna") QnA qna);

    @Query(
        value = "SELECT COUNT(q) " +
                "FROM QnALikeDislike q " +
                "WHERE q.id.qna = :qna AND q.status = 'B' AND q.isDeleted = false"
    )
    int countQnADislikeByQnAId(@Param("qna") QnA qna);

    @Modifying
    @Query(
        value = "UPDATE QnALikeDislike " +
                "SET isDeleted = IF(isDeleted, false, true) " +
                "WHERE id.member = :member AND id.qna = :qna"
    )
    void updateDeleteStatus(@Param("member")Member member, @Param("qna") QnA qna);
}