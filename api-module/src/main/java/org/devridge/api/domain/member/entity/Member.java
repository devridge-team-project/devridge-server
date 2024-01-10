package org.devridge.api.domain.member.entity;

import lombok.Builder;
import lombok.Getter;
import org.devridge.api.domain.AbstractTimeEntity;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Entity
@Table(name = "member")
@Getter
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

    @Builder
    public Member(String email, String password, String provider, String nickname, String roles, String introduction, String profileImageUrl, boolean isDeleted) {
        this.email = email;
        this.password = password;
        this.provider = provider;
        this.nickname = nickname;
        this.roles = roles;
        this.introduction = introduction;
        this.profileImageUrl = profileImageUrl;
        this.isDeleted = isDeleted;
    }

    protected Member() {
    }

    public void softDelete(){
        isDeleted = true;
    }
}
