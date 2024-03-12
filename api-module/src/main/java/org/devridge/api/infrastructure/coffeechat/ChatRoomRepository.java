package org.devridge.api.infrastructure.coffeechat;

import org.devridge.api.domain.coffeechat.entity.ChatRoom;
import org.devridge.api.domain.member.entity.Member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    @Query(
        value = "SELECT MAX(id) " +
                "FROM ChatRoom " +
                "WHERE firstMember = :member " +
                "   OR secondMember = :member"
    )
    Optional<Long> findMaxId(@Param("member") Member member);
}
