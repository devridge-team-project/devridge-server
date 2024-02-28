package org.devridge.api.domain.coffeechat.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.devridge.api.domain.coffeechat.entity.id.ChatRoomId;
import org.devridge.api.domain.member.entity.Member;
import org.devridge.common.entity.BaseEntity;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@SQLDelete(sql = "UPDATE chat_message SET is_deleted = true WHERE id = ?")
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "chat_message")
@Entity
public class ChatMessage extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id")
    @JsonBackReference
    private ChatRoom chatRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @NotNull
    private String content;

    @Builder
    public ChatMessage(ChatRoom chatRoom, Member member, String content) {
        this.chatRoom = chatRoom;
        this.member = member;
        this.content = content;
    }
}
