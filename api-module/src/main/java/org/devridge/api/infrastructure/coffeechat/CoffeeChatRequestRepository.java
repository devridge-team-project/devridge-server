package org.devridge.api.infrastructure.coffeechat;

import org.devridge.api.domain.coffeechat.entity.CoffeeChatRequest;
import org.devridge.api.domain.member.entity.Member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CoffeeChatRequestRepository extends JpaRepository<CoffeeChatRequest, Long> {

    @Query(
        value = "SELECT MAX(id) " +
                "FROM CoffeeChatRequest " +
                "WHERE fromMember = :member " +
                "   OR toMember = :member"
    )
    Optional<Long> findMaxId(@Param("member") Member member);
}
