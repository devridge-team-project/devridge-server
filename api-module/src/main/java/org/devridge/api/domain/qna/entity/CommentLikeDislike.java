package org.devridge.api.domain.qna.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.devridge.api.domain.member.entity.Member;
import org.devridge.api.domain.qna.dto.type.LikeStatus;
import org.devridge.api.domain.qna.entity.id.CommentLikeDislikeId;
import org.devridge.common.entity.BaseTimeEntity;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Getter
@DynamicInsert
@SQLDelete(sql = "UPDATE qna_comment_like_dislike SET is_deleted = true WHERE id = ?")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "qna_comment_like_dislike")
@Entity
public class CommentLikeDislike extends BaseTimeEntity {

    @EmbeddedId
    private CommentLikeDislikeId id;

    @Enumerated(EnumType.STRING)
    private LikeStatus status;

    @Builder
    public CommentLikeDislike(CommentLikeDislikeId id, LikeStatus status) {
        this.id = id;
        this.status = status;
    }
}
