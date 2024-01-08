package org.devridge.api.domain.member.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devridge.api.domain.AbstractTimeEntity;

import javax.persistence.*;

@Entity
@Table(name = "refresh_token")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RefreshToken extends AbstractTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String refreshToken;

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;
}
