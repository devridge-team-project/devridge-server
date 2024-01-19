package org.devridge.api.domain.community.entity;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devridge.api.domain.AbstractTimeEntity;
import org.devridge.api.domain.member.entity.Member;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@SQLDelete(sql = "UPDATE community SET is_deleted = true WHERE id = ?")
@Where(clause = "is_deleted = false")
@Table(name = "community")
public class Community extends AbstractTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "member_id")
    private Long memberId;

    private String title;

    private String content;

    private boolean isDeleted;

    private Long views;

    @OneToMany(mappedBy = "community")
    private List<CommunityComment> communityComment = new ArrayList<>();

    @OneToMany(mappedBy = "community")
    private List<CommunityScrap> communityScrap = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", insertable = false, updatable = false)
    private Member member;

    public void updateCommunity(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
