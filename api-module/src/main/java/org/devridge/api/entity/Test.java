package org.devridge.api.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@DynamicInsert
@SQLDelete(sql = "UPDATE test SET is_deleted = true WHERE id = ?")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "test")
@Entity
public class Test {

    @Id
    private Long id;

    // TODO: Member Entity mapping
    private Long memberId;

    @NotNull
    private String title;

    @NotNull
    private String content;
}
