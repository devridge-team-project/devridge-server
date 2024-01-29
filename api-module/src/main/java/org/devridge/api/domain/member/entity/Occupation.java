package org.devridge.api.domain.member.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devridge.common.entity.BaseEntity;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "occupation")
@SQLDelete(sql = "UPDATE occupation SET is_deleted = true WHERE id = ?")
@Where(clause = "is_deleted = false")
public class Occupation extends BaseEntity {

    @Getter
    @NotNull
    private String occupation;
}
