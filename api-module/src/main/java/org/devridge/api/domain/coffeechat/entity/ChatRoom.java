package org.devridge.api.domain.coffeechat.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.devridge.api.domain.member.entity.Member;
import org.devridge.common.entity.BaseEntity;

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
public class ChatRoom extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "first_member_id")
    private Member firstMember;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "second_member_id")
    private Member secondMember;

    @JsonManagedReference
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "chatRoom")
    private List<ChatMessage> messages = new ArrayList<>();

    public ChatRoom(Member firstMember, Member secondMember) {
        this.firstMember = firstMember;
        this.secondMember = secondMember;
    }
}
