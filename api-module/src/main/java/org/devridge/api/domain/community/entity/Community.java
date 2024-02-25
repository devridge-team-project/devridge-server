package org.devridge.api.domain.community.entity;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devridge.api.domain.member.entity.Member;
import org.devridge.common.entity.BaseEntity;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@DynamicInsert
@SQLDelete(sql = "UPDATE community SET is_deleted = true WHERE id = ?")
@Where(clause = "is_deleted = false")
@Table(name = "community")
public class Community extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", updatable = false)
    private Member member;

    private String title;

    private String content;

    @ColumnDefault("0")
    private Long views;

    @ColumnDefault("0")
    private Long likeCount;

    @ColumnDefault("0")
    private Long dislikeCount;

    @OneToMany(mappedBy = "community", cascade = CascadeType.ALL)
    private List<CommunityComment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "community", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommunityHashtag> hashtags = new ArrayList<>();

    @OneToMany(mappedBy = "community", cascade = CascadeType.ALL)
    private List<CommunityScrap> scraps = new ArrayList<>();

    private String images;

    @Builder
    public Community(Member member, String title, String content, String images) {
        this.member = member;
        this.title = title;
        this.content = content;
        this.images = images;
    }


    public void updateCommunity(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
