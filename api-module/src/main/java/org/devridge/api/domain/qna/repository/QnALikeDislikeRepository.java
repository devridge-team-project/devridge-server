package org.devridge.api.domain.qna.repository;

import org.devridge.api.domain.member.entity.Member;
import org.devridge.api.domain.qna.entity.QnA;
import org.devridge.api.domain.qna.entity.QnALikeDislike;
import org.devridge.api.domain.qna.entity.id.QnALikeDislikeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface QnALikeDislikeRepository extends JpaRepository<QnALikeDislike, QnALikeDislikeId> {

    @Modifying(clearAutomatically = true)
    @Query(
            value = "UPDATE QnALikeDislike " +
                    "SET status = 'G' " +
                    "WHERE id.member = :member AND id.qna = :qna"
    )
    void updateQnALikeStatusToGood(@Param("member")Member member, @Param("qna") QnA qna);

    @Modifying(clearAutomatically = true)
    @Query(
            value = "UPDATE QnALikeDislike " +
                    "SET status = 'B' " +
                    "WHERE id.member = :member AND id.qna = :qna"
    )
    void updateQnALikeStatusToBad(@Param("member")Member member, @Param("qna") QnA qna);

    @Modifying(clearAutomatically = true)
    @Query(
            value = "UPDATE QnALikeDislike " +
                    "SET isDeleted = true " +
                    "WHERE id.member = :member AND id.qna = :qna"
    )
    void deleteById(@Param("member")Member member, @Param("qna") QnA qna);
}