package org.devridge.api.domain.member.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devridge.api.constant.Role;
import org.devridge.common.dto.BaseEntity;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "member")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {
    @NotNull
    private String email;

    @NotNull
    private String password;

    @NotNull
    @ColumnDefault("normal")
    private String provider;

    @NotNull
    private String nickname;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Role role;

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

    public void softDelete(){
        isDeleted = true;
    }
}
