package org.devridge.api.domain.member.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devridge.api.constant.Role;
import org.devridge.api.domain.skill.entity.MemberSkill;
import org.devridge.common.dto.BaseEntity;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "member")
@Getter
@DynamicInsert
@SQLDelete(sql = "UPDATE qna SET is_deleted = true WHERE id = ?")
@Where(clause = "is_deleted = false")
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
    private Role roles;

    @Column(name = "introduction", nullable = true)
    private String introduction;

    @Column(name = "profile_image_url", nullable = true)
    private String profileImageUrl;

    @Getter
    @OneToMany(mappedBy = "member", fetch = FetchType.EAGER)
    private List<MemberSkill> memberSkills = new ArrayList<>();

    @Builder
    public Member(String email, String password, String provider, String nickname, Role roles, String introduction, String profileImageUrl) {
        this.email = email;
        this.password = password;
        this.provider = provider;
        this.nickname = nickname;
        this.roles = roles;
        this.introduction = introduction;
        this.profileImageUrl = profileImageUrl;
    }
}
