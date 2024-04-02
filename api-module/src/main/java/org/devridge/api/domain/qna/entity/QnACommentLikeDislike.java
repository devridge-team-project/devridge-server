package org.devridge.api.domain.qna.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.devridge.api.domain.qna.type.LikeStatus;
import org.devridge.api.domain.qna.entity.id.QnACommentLikeDislikeId;
import org.devridge.api.common.entity.BaseTimeEntity;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;

@Getter
@DynamicInsert
@SQLDelete(sql = "UPDATE qna_comment_like_dislike SET is_deleted = true WHERE id = ?")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "qna_comment_like_dislike")
@Entity
public class QnACommentLikeDislike extends BaseTimeEntity {

    @EmbeddedId
    private QnACommentLikeDislikeId id;

    @Enumerated(EnumType.STRING)
    private LikeStatus status;

    @Builder
    public QnACommentLikeDislike(QnACommentLikeDislikeId id, LikeStatus status) {
        this.id = id;
        this.status = status;
    }
}
