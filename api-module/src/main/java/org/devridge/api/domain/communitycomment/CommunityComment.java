package org.devridge.api.domain.communitycomment;

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
import lombok.Getter;
import org.devridge.api.domain.communitycommentlikedislike.CommunityCommentLikeDislike;
import org.devridge.api.domain.community.Community;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

@Getter
@Entity
public class CommunityComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "communityComment")
    private List<CommunityCommentLikeDislike> communityCommentLikeDislike = new ArrayList<>();

    @Column(name = "community_id")
    private Long communityId;

    @ManyToOne
    @JoinColumn(name = "community_id", insertable = false, updatable = false)
    private Community community;

    private String content;

    private Boolean isDeleted;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
