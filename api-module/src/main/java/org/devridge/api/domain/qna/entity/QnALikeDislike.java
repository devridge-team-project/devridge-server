package org.devridge.api.domain.qna.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.devridge.api.domain.member.entity.Member;
import org.devridge.api.domain.qna.dto.type.LikeStatus;
import org.devridge.api.domain.qna.entity.id.QnALikeDislikeId;
import org.devridge.common.dto.BaseTimeEntity;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Getter
@SQLDelete(sql = "UPDATE qna_scrap SET is_deleted = true WHERE id = ?")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "qna_like_dislike")
@DynamicInsert
@Entity
public class QnALikeDislike extends BaseTimeEntity {

    @EmbeddedId
    private QnALikeDislikeId id;

    @Enumerated(EnumType.STRING)
    private LikeStatus status;

    public QnALikeDislike(QnALikeDislikeId id, LikeStatus status) {
        this.id = id;
        this.status = status;
    }
}
