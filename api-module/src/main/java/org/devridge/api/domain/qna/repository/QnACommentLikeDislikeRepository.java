package org.devridge.api.domain.qna.repository;

import org.devridge.api.domain.member.entity.Member;
import org.devridge.api.domain.qna.dto.type.LikeStatus;
import org.devridge.api.domain.qna.entity.QnACommentLikeDislike;
import org.devridge.api.domain.qna.entity.QnAComment;
import org.devridge.api.domain.qna.entity.id.QnACommentLikeDislikeId;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface QnACommentLikeDislikeRepository extends JpaRepository<QnACommentLikeDislike, QnACommentLikeDislikeId> {

    @Modifying
    @Query(
        value = "UPDATE QnACommentLikeDislike " +
                "SET status = :status, isDeleted = :isDeleted " +
                "WHERE id.member = :member AND id.qnaComment = :qnaComment"
    )
    void updateQnACommentLikeStatusToGoodOrBad(
        @Param("member") Member member,
        @Param("qnaComment") QnAComment qnaComment,
        @Param("isDeleted") Boolean isDeleted,
        @Param("status") LikeStatus status
    );

    @Modifying
    @Query(
        value = "UPDATE QnACommentLikeDislike " +
                "SET isDeleted = IF(isDeleted, false, true) " +
                "WHERE id.member = :member AND id.qnaComment = :qnaComment"
    )
    void updateDeleteStatus(@Param("member")Member member, @Param("qnaComment") QnAComment qnaComment);
}
