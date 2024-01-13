package org.devridge.api.domain.member.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devridge.api.domain.AbstractTimeEntity;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

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

    // TODO: [필수] 이부분 고정타입일 것 같은데 enum 검증하도록 바꿔주시면 더 좋습니다.
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
