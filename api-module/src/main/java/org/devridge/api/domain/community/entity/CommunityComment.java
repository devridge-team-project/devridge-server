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
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devridge.api.domain.AbstractTimeEntity;
import org.devridge.api.domain.member.entity.Member;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@DynamicInsert
@SQLDelete(sql = "UPDATE community_comment SET is_deleted = true WHERE id = ?")
@Where(clause = "is_deleted = false")
public class CommunityComment extends AbstractTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "communityComment")
    private List<CommunityCommentLikeDislike> communityCommentLikeDislike = new ArrayList<>();

    @Column(name = "community_id")
    private Long communityId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "community_id", insertable = false, updatable = false)
    private Community community;

    @Column(name = "member_id")
    private Long memberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", insertable = false, updatable = false)
    private Member member;

    private String content;

    private Boolean isDeleted;

    private Long likeCount;

    public void countLikeDislike(List<CommunityCommentLikeDislike> list) {
        long sum = 0;
        for (CommunityCommentLikeDislike like : list) {
            if (like.getStatus() == LikeStatus.B) {
                --sum;
            }
            if (like.getStatus() == LikeStatus.G) {
                ++sum;
            }
        }
        this.likeCount = sum;
    }

    public void updateComment(String content) {
        this.content = content;
    }
}