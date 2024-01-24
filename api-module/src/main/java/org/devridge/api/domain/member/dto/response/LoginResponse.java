package org.devridge.api.domain.member.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devridge.api.security.dto.TokenResponse;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginResponse {

    private TokenResponse accessToken;
    private MemberResponse member;

    @Builder
    public LoginResponse(TokenResponse accessToken, MemberResponse member) {
        this.accessToken = accessToken;
        this.member = member;
    }
}
