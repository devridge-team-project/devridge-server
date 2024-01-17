package org.devridge.api.domain.qna.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.devridge.api.domain.qna.entity.key.QnAScrapKey;
import org.devridge.common.dto.BaseTimeEntity;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@SQLDelete(sql = "UPDATE qna_scrap SET is_deleted = true WHERE id = ?")
@Where(clause = "is_deleted = false")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "qna_scrap")
@DynamicInsert
@Entity
public class QnAScrap extends BaseTimeEntity {

    @EmbeddedId
    private QnAScrapKey id;

    @Builder
    public QnAScrap(Long memberId, QnA qna) {
        this.id = new QnAScrapKey(memberId, qna);
    }
}
