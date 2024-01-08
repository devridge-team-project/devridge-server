package org.devridge.api.domain.community;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Getter;
import org.devridge.api.domain.communitycomment.CommunityComment;
import org.devridge.api.domain.communityscrap.CommunityScrap;
import org.devridge.api.githubsociallogintemp.domain.Member;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;


@Entity
@Getter
@Table(name = "community")
public class Community {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "member_id")
    private Long memberId;

    private String title;

    private String content;

    private Boolean isDeleted;

    private Long views;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "community")
    private List<CommunityComment> communityComment = new ArrayList<>();

    @OneToMany(mappedBy = "community")
    private List<CommunityScrap> communityScrap = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "member_id", insertable = false, updatable = false)
    private Member member;

}
