package org.devridge.api.infrastructure.qna;

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
        @Param("status") LikeStatus status
    );

    @Query(
        value = "SELECT COUNT(q) " +
                "FROM QnALikeDislike q " +
                "WHERE q.id.qna = :qna AND q.status = :status AND q.isDeleted = :isDeleted"
    )
    int countQnALikeOrDislikeByQnAId(
        @Param("qna") QnA qna,
        @Param("status") LikeStatus status,
        @Param("isDeleted") Boolean isDeleted
    );

    @Modifying
    @Query(
        value = "UPDATE QnALikeDislike " +
                "SET isDeleted = IF(isDeleted, false, true) " +
                "WHERE id.member = :member AND id.qna = :qna"
    )
    void updateDeleteStatus(@Param("member")Member member, @Param("qna") QnA qna);
}