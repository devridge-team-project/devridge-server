package org.devridge.api.infrastructure.auth;

import org.devridge.api.domain.auth.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    @Query("select token FROM RefreshToken token WHERE token.member.id = :memberId")
    Optional<RefreshToken> findByMemberId(@Param("memberId") Long memberId);
    Optional<RefreshToken> findById(Long id);
}
