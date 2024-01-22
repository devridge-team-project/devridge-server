package org.devridge.api.domain.community.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.devridge.api.domain.member.entity.Member;
import org.devridge.common.dto.BaseEntity;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@SuperBuilder
@Entity
@DynamicInsert
@SQLDelete(sql = "UPDATE community SET is_deleted = true WHERE id = ?")
@Where(clause = "is_deleted = false")
@Table(name = "community")
public class Community extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", insertable = false, updatable = false)
    private Member member;

    private String title;

    private String content;

    private Long views;

    public void updateCommunity(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
