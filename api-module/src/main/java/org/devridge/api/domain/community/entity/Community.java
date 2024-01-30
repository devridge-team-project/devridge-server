package org.devridge.api.domain.community.entity;

import java.util.ArrayList;
import java.util.List;
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

    private Long views;

    private Long likeCount;

    private Long dislikeCount;

    @OneToMany(mappedBy = "community")
    private List<CommunityComment> comments = new ArrayList<>();

    @Builder
    public Community(Member member, String title, String content) {
        this.member = member;
        this.title = title;
        this.content = content;
    }


    public void updateCommunity(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
