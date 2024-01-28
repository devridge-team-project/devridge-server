package org.devridge.api.domain.member.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginResponse {

    private String accessToken;
    private MemberResponse member;

    @Builder
    public LoginResponse(String accessToken, MemberResponse member) {
        this.accessToken = accessToken;
        this.member = member;
    }
}
