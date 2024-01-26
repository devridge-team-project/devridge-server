package org.devridge.api.domain.emailverification.dto.request;

import lombok.Getter;

import javax.validation.constraints.Pattern;

@Getter
public class CheckEmailVerification {

    private String email;

    @Pattern(regexp = "^[0-9]{4}$", message = "검증 코드는 4자리 숫자여야 합니다.")
    private String verificationCode;
}
