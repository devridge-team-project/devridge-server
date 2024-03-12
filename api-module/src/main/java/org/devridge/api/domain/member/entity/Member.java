package org.devridge.api.domain.member.entity;

import lombok.*;
import org.devridge.api.common.type.Role;
import org.devridge.api.domain.skill.entity.MemberSkill;
import org.devridge.api.common.entity.BaseEntity;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "member")
@Getter
@DynamicInsert
@DynamicUpdate
@SQLDelete(sql = "UPDATE member SET is_deleted = true WHERE id = ?")
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

    @Column(name = "introduction")
    private String introduction;

    @Column(name = "profile_image_url")
    private String profileImageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "occupation_id")
    @NotNull
    private Occupation occupation;

    @Getter
    @OneToMany(mappedBy = "member")
    private List<MemberSkill> memberSkills = new ArrayList<>();

    @Builder
    public Member(String email, String password, String provider, String nickname, Role roles, String introduction, String profileImageUrl, Occupation occupation) {
        this.email = email;
        this.password = password;
        this.provider = provider;
        this.nickname = nickname;
        this.roles = roles;
        this.introduction = introduction;
        this.profileImageUrl = profileImageUrl;
        this.occupation = occupation;
    }

    public void changePassword(String encodePassword) {
        this.password = encodePassword;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }
}