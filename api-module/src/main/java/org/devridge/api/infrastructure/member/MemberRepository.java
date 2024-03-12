package org.devridge.api.infrastructure.member;

import org.devridge.api.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmailAndProvider(String email, String provider);

    Optional<Member> findByNickname(String nickname);
}
