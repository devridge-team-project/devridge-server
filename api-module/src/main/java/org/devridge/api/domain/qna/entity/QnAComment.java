package org.devridge.api.domain.qna.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.devridge.common.dto.BaseEntity;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@SQLDelete(sql = "UPDATE qna_comment SET is_deleted = true WHERE id = ?")
@Where(clause = "is_deleted = false")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "qna_comment")
@Entity
public class QnAComment extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "qna_id")
    @JsonBackReference
    private QnA qna;

    @NotNull
    private String content;

    @Builder
    public QnAComment(QnA qna, String content) {
        this.qna = qna;
        this.content = content;
    }
}
