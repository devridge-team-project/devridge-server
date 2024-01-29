package org.devridge.api.domain.community.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@SQLDelete(sql = "UPDATE community_comment SET is_deleted = true WHERE id = ?")
@Where(clause = "is_deleted = false")
public class CommunityComment extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "community_id", updatable = false)
    private Community community;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", updatable = false)
    private Member member;

    private String content;

    private Long likeCount;

    private Long dislikeCount;

    @Builder
    public CommunityComment(Community community, Member member, String content) {
        this.community = community;
        this.member = member;
        this.content = content;
    }


    public void updateComment(String content) {
        this.content = content;
    }
}