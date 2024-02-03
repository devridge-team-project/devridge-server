package org.devridge.api.domain.sociallogin.dto.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SocialLoginRequest {

    @NotBlank(message = "provider must not be blank")
    @Pattern(regexp = "^(naver|kakao|google)$", message = "provider 는 다음 중 하나여야 합니다: naver, kakao, google")
    private String provider;

    @NotBlank(message = "code must not be blank")
    private String code;

    @Builder
    public SocialLoginRequest(String provider, String code) {
        this.provider = provider;
        this.code = code;
    }
}
