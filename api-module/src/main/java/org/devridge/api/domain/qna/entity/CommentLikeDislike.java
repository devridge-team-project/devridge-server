package org.devridge.api.domain.qna.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.devridge.api.domain.member.entity.Member;
import org.devridge.api.domain.qna.validator.type.LikeStatus;
import org.devridge.api.domain.qna.entity.id.CommentLikeDislikeId;
import org.devridge.common.dto.BaseTimeEntity;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Getter
@DynamicInsert
@SQLDelete(sql = "UPDATE qna SET is_deleted = true WHERE id = ?")
@Where(clause = "is_deleted = false")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "comment_like_dislike")
@Entity
public class CommentLikeDislike extends BaseTimeEntity {

    @EmbeddedId
    private CommentLikeDislikeId id;

    @Enumerated(EnumType.STRING)
    private LikeStatus status;

    @Builder
    public CommentLikeDislike(Member member, QnAComment qnaComment, LikeStatus status) {
        this.id = new CommentLikeDislikeId(member, qnaComment);
        this.status = status;
    }
}
