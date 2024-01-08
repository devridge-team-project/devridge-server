package org.devridge.api.domain.member.repository;

import org.devridge.api.domain.member.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    @Query("select rf FROM RefreshToken rf WHERE rf.member.id = :memberId")
    Optional<RefreshToken> findByMemberId(@Param("memberId") Long memberId);
    Optional<RefreshToken> findById(Long id);
}
