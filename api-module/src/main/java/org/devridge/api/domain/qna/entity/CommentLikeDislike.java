package org.devridge.api.domain.qna.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devridge.api.domain.qna.entity.key.CommentLikeDislikeKey;
import org.devridge.common.dto.BaseTimeEntity;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Getter
@DynamicInsert
@SQLDelete(sql = "UPDATE qna SET is_deleted = true WHERE id = ?")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class CommentLikeDislike extends BaseTimeEntity {

    @EmbeddedId
    private CommentLikeDislikeKey id;

    @Column(name = "is_deleted")
    @ColumnDefault("false")
    private Boolean isDeleted;
}
