package org.devridge.api.domain.member.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devridge.api.domain.member.entity.Member;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class CreateMemberResponse {

    private Long userId;
    private String email;
    private String nickname;
    private String profileImageUrl;
    private LocalDateTime createdAt;

    public static CreateMemberResponse of(Member member){
        return CreateMemberResponse.builder()
                .userId(member.getId())
                .email(member.getEmail())
                .nickname(member.getNickname())
                .profileImageUrl(member.getProfileImageUrl())
                .createdAt(member.getCreatedAt())
                .build();
    }

}
