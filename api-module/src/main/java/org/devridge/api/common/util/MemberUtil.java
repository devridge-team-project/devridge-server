package org.devridge.api.common.util;

import org.devridge.api.domain.member.entity.Member;
import org.devridge.api.common.dto.FindWriterInformation;

import javax.persistence.EntityNotFoundException;

public class MemberUtil {

    public static FindWriterInformation toMember(Member member) {
        try {
            return FindWriterInformation.builder()
                .id(member.getId())
                .nickname(member.getNickname())
                .introduction(member.getIntroduction())
                .profileImageUrl(member.getProfileImageUrl())
                .build();
        } catch (EntityNotFoundException e) {
            return FindWriterInformation.builder()
                .id(0L)
                .nickname("탈퇴한 사용자")
                .introduction("탈퇴한 사용자입니다.")
                .profileImageUrl("default_user.png")
                .build();
        }
    }
}
