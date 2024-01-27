package org.devridge.api.domain.qna.repository;

import org.devridge.api.domain.member.entity.Member;
import org.devridge.api.domain.qna.entity.CommentLikeDislike;
import org.devridge.api.domain.qna.entity.QnA;
import org.devridge.api.domain.qna.entity.QnAComment;
import org.devridge.api.domain.qna.entity.id.CommentLikeDislikeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentLikeDislikeRepository extends JpaRepository<CommentLikeDislike, CommentLikeDislikeId> {

    @Modifying
    @Query(
        value = "UPDATE CommentLikeDislike " +
                "SET status = 'G' " +
                "WHERE id.member = :member AND id.qnaComment = :qnaComment AND isDeleted = false"
    )
    void updateQnACommentLikeStatusToGood(@Param("member") Member member, @Param("qnaComment") QnAComment qnaComment);

    @Modifying
    @Query(
        value = "UPDATE CommentLikeDislike " +
                "SET status = 'B' " +
                "WHERE id.member = :member AND id.qnaComment = :qnaComment AND isDeleted = false"
    )
    void updateQnACommentLikeStatusToBad(@Param("member")Member member, @Param("qnaComment") QnAComment qnaComment);

    @Modifying
    @Query(
            value = "UPDATE CommentLikeDislike " +
                    "SET isDeleted = true " +
                    "WHERE id.member = :member AND id.qnaComment = :qnaComment"
    )
    void deleteById(@Param("member")Member member, @Param("qnaComment") QnAComment qnaComment);

    @Modifying
    @Query(
            value = "UPDATE CommentLikeDislike " +
                    "SET isDeleted = false " +
                    "WHERE id.member = :member AND id.qnaComment = :qnaComment"
    )
    int recoverLikeDislike(@Param("member")Member member, @Param("qnaComment") QnAComment qnaComment);
}
