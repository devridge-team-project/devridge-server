package org.devridge.api.domain.qna.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.devridge.api.domain.member.entity.Member;
import org.devridge.api.domain.qna.dto.type.LikeStatus;
import org.devridge.api.domain.qna.entity.id.QnALikeDislikeId;
import org.devridge.common.dto.BaseTimeEntity;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Getter
@SQLDelete(sql = "UPDATE qna_scrap SET is_deleted = true WHERE id = ?")
@Where(clause = "is_deleted = false")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "qna_like_dislike")
@Entity
public class QnALikeDislike extends BaseTimeEntity {

    @EmbeddedId
    private QnALikeDislikeId id;

    @Enumerated(EnumType.STRING)
    private LikeStatus status;

    @Column(name = "is_deleted", columnDefinition = "TINYINT(1)")
    @ColumnDefault("false")
    private Boolean isDeleted;

    @Builder
    public QnALikeDislike(Member member, QnA qna) {
        this.id = new QnALikeDislikeId(member, qna);
    }
}
