package org.devridge.api.domain.qna.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.devridge.api.domain.qna.entity.key.QnAScrapKey;
import org.devridge.common.dto.BaseTimeEntity;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Getter
@SQLDelete(sql = "UPDATE qna_scrap SET is_deleted = true WHERE id = ?")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class QnAScrap extends BaseTimeEntity {

    @EmbeddedId
    private QnAScrapKey id;

    @Column(name = "is_deleted")
    @ColumnDefault("false")
    private Boolean isDeleted;
}
