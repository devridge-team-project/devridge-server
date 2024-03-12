package org.devridge.api.common.util;

import org.devridge.api.common.dto.UserInformation;
import org.devridge.api.domain.member.entity.Member;

import javax.persistence.EntityNotFoundException;

public class MemberUtil {

    public static UserInformation toMember(Member member) {
        try {
            return UserInformation.builder()
                .id(member.getId())
                .nickname(member.getNickname())
                .introduction(member.getIntroduction())
                .profileImageUrl(member.getProfileImageUrl())
                .build();
        } catch (EntityNotFoundException e) {
            return UserInformation.builder()
                .id(0L)
                .nickname("탈퇴한 사용자")
                .introduction("탈퇴한 사용자입니다.")
                .profileImageUrl("default_user.png")
                .build();
        }
    }
}
