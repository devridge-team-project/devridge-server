package org.devridge.api.domain.member.entity;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devridge.api.domain.AbstractTimeEntity;
import org.devridge.api.domain.community.entity.Community;
import org.devridge.api.domain.community.entity.CommunityComment;
import org.devridge.api.domain.community.entity.CommunityCommentLikeDislike;
import org.devridge.api.domain.community.entity.CommunityScrap;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "member")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends AbstractTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "provider")
    @ColumnDefault("normal")
    private String provider;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Column(name = "roles", nullable = false)
    private String roles;

    @Column(name = "introduction", nullable = true)
    private String introduction;

    @Column(name = "profile_image_url", nullable = true)
    private String profileImageUrl;

    @Column(columnDefinition = "TINYINT(1)")
    private boolean isDeleted;

    @OneToMany(mappedBy = "member")
    private List<Community> community = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<CommunityComment> communityComment = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<CommunityScrap> communityScrap = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<CommunityCommentLikeDislike> communityCommentLikeDislike = new ArrayList<>();

    @Builder
    public Member(Long id, String email, String password, String provider, String nickname, String roles,
        String introduction,
        String profileImageUrl, boolean isDeleted) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.provider = provider;
        this.nickname = nickname;
        this.roles = roles;
        this.introduction = introduction;
        this.profileImageUrl = profileImageUrl;
        this.isDeleted = isDeleted;
    }

    public void softDelete() {
        isDeleted = true;
    }
}
