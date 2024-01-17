package org.devridge.api.domain.qna.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.devridge.api.domain.qna.dto.type.LikeStatus;
import org.devridge.api.domain.qna.entity.key.CommentLikeDislikeKey;
import org.devridge.common.dto.BaseTimeEntity;

import org.hibernate.annotations.ColumnDefault;
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
    private CommentLikeDislikeKey id;

    @Enumerated(EnumType.STRING)
    private LikeStatus status;

    @Builder
    public CommentLikeDislike(Long memberId, QnAComment qnaComment, LikeStatus status) {
        this.id = new CommentLikeDislikeKey(memberId, qnaComment);
        this.status = status;
    }
}
