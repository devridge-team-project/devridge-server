package org.devridge.api.domain.coffeechat.entity.id;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.devridge.api.domain.member.entity.Member;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import java.io.Serializable;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class ChatRoomId implements Serializable {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "first_member_id")
    private Member firstMember;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "second_member_id")
    private Member secondMember;
}
