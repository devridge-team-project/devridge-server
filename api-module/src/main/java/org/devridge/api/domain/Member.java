package org.devridge.api.domain;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "member")
public class Member extends AbstractTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "salt", nullable = false)
    @Type(type = "uuid-char")
    private UUID salt;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Column(name = "roles", nullable = false)
    private String roles;

    @Column(name = "introduction", nullable = true)
    private String introduction;

    @Column(name = "profile_image_url", nullable = true)
    private String profileImageUrl;

    private boolean isDeleted;

}
