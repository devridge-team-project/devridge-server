package org.devridge.api.domain.qna.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.*;

import org.devridge.api.domain.member.entity.Member;
import org.devridge.common.entity.BaseEntity;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;

@Getter
@DynamicInsert
@SQLDelete(sql = "UPDATE qna SET is_deleted = true WHERE id = ?")
@Where(clause = "is_deleted = false")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "qna")
@Builder(toBuilder = true)
@AllArgsConstructor
@Entity
public class QnA extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @NotNull
    private String title;

    @NotNull
    private String content;

    @ColumnDefault("0")
    private Integer likes;

    @ColumnDefault("0")
    private Integer dislikes;

    @ColumnDefault("0")
    private Integer views;

    private String imageUrl;

    @JsonManagedReference
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "qna")
    List<QnAComment> comments = new ArrayList<>();
}
