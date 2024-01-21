package org.devridge.api.domain.qna.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.devridge.api.domain.member.entity.Member;
import org.devridge.common.dto.BaseEntity;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@SQLDelete(sql = "UPDATE qna_comment SET is_deleted = true WHERE id = ?")
@Where(clause = "is_deleted = false")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "qna_comment")
@DynamicInsert
@Entity
public class QnAComment extends BaseEntity {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "qna_id")
    @JsonBackReference
    private QnA qna;

    @NotNull
    private String content;

    @ColumnDefault("0")
    private Integer likes;

    @ColumnDefault("0")
    private Integer dislikes;

    @Builder
    public QnAComment(QnA qna, Member member, String content) {
        this.member = member;
        this.qna = qna;
        this.content = content;
    }
}
