package org.devridge.api.domain.coffeechat.repository;

import org.devridge.api.domain.coffeechat.entity.ChatMessage;
import org.devridge.api.domain.coffeechat.entity.ChatRoom;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    @Query(
        value = "SELECT MAX(id) " +
                "FROM ChatMessage " +
                "WHERE chatRoom = :chatRoom"
    )
    Optional<Long> findMaxId(@Param("chatRoom") ChatRoom chatRoom);
}
