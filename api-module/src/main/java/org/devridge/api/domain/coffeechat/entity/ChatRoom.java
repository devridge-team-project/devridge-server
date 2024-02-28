package org.devridge.api.domain.coffeechat.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.devridge.api.domain.coffeechat.entity.id.ChatRoomId;
import org.devridge.common.entity.BaseTimeEntity;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE chat_room SET is_deleted = true WHERE id = ?")
@Table(name = "chat_room")
public class ChatRoom extends BaseTimeEntity {

    @EmbeddedId
    private ChatRoomId id;

    @JsonManagedReference
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "chatRoom")
    private List<ChatMessage> messages = new ArrayList<>();

    public ChatRoom(ChatRoomId id) {
        this.id = id;
    }
}
