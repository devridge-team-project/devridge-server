package org.devridge.api.domain.member.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
public class ChangePasswordRequest {

    @NotBlank(message = "빈 패스워드를 입력할 수 없습니다.")
    @Size(min = 8, max = 20, message = "패스워드는 8자 이상 20자 이하이어야 합니다.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,20}$", message = "패스워드는 영문자 + 숫자 조합이어야 합니다.")
    private String password;
}
