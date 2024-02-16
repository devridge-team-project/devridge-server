package org.devridge.api.domain.community.mapper;

import javax.persistence.EntityNotFoundException;
import org.devridge.api.domain.community.dto.response.MemberInfoResponse;
import org.devridge.api.domain.member.entity.Member;
import org.springframework.stereotype.Component;

@Component
public class MemberInfoMapper {

    public MemberInfoResponse toMemberInfoResponse(Member member) {
        try {
            return MemberInfoResponse.builder()
                    .memberId(member.getId())
                    .nickName(member.getNickname())
                    .introduction(member.getIntroduction())
                    .profileImageUrl(member.getProfileImageUrl())
                    .build();
        } catch (EntityNotFoundException e) {
            return MemberInfoResponse.builder()
                    .memberId(0L)
                    .nickName("탈퇴 회원")
                    .introduction("탈퇴한 회원입니다.")
                    .profileImageUrl("default.png")
                    .build();
        }
    }
}
