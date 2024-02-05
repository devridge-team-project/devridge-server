package org.devridge.api.domain.sociallogin.dto.request;

import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
public class SocialLoginRequest {

    @NotBlank(message = "provider must not be blank")
    @Pattern(regexp = "^(naver|kakao|google)$", message = "provider 는 다음 중 하나여야 합니다: naver, kakao, google")
    private String provider;

    @NotBlank(message = "code must not be blank")
    private String code;
}
