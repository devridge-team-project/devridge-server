package org.devridge.api.domain.member.entity;

import lombok.Builder;
import lombok.Getter;
import org.devridge.api.domain.AbstractTimeEntity;

import javax.persistence.*;

@Entity
@Table(name = "refresh_token")
@Getter
public class RefreshToken extends AbstractTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String refreshToken;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public RefreshToken(String refreshToken, Member member) {
        this.refreshToken = refreshToken;
        this.member = member;
    }

    protected RefreshToken() {
    }
}
