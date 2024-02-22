package org.devridge.api.domain.community.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.devridge.api.domain.member.entity.Member;
import org.devridge.common.entity.BaseEntity;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE study SET is_deleted = true WHERE id = ?")
@Where(clause = "is_deleted = false")
public class Study extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", updatable = false)
    private Member member;

    private String title;

    private String content;

    @ColumnDefault("0")
    private Long views;

    @ColumnDefault("0")
    private Long likes;

    @ColumnDefault("0")
    private Long dislikes;

    private String category;

    private String images;

    private String location;

    @ColumnDefault("0")
    private Integer totalPeople;

    @ColumnDefault("0")
    private Integer currentPeople;

    @Builder
    public Study(Member member, String title, String content, String category, String images, String location, Integer totalPeople, Integer currentPeople) {
        this.member = member;
        this.title = title;
        this.content = content;
        this.category = category;
        this.images = images;
        this.location = location;
        this.totalPeople = totalPeople;
        this.currentPeople = currentPeople;
    }
}
