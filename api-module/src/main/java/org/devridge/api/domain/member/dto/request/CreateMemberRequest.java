package org.devridge.api.domain.member.dto.request;

import lombok.Getter;

import javax.validation.constraints.*;
import java.util.List;

@Getter
public class CreateMemberRequest {

    @NotBlank(message = "이메일을 입력해주세요.")
    @Email(message = "유효하지 않은 이메일 형식입니다.")
    private String email;

    @NotBlank(message = "패스워드를 입력해주세요.")
    @Size(min = 8, max = 20, message = "패스워드는 8자 이상 20자 이하이어야 합니다.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,20}$", message = "패스워드는 영문자 + 숫자 조합이어야 합니다.")
    private String password;

    @NotBlank(message = "provider를 입력해주세요.")
    private String provider;

    @NotBlank(message = "닉네임을 입력해주세요.")
    @Pattern(regexp = "^[a-zA-Z가-힣0-9 ]*$", message = "닉네임 입력 형식이 올바르지 않습니다.")
    @Size(min = 3, max = 12, message = "닉네임은 (3~12) 글자 입니다.")
    private String nickname;

    @NotBlank(message = "자기소개를 입력해주세요.")
    @Size(min = 5, max = 25, message = "자기소개는 5 ~ 25자내로 입력해주세요.")
    @Pattern(regexp = "^[a-zA-Z가-힣 ]*$", message = "자기소개 입력 형식이 올바르지 않습니다.")
    private String introduction;

    @Size(max = 5, message = "스킬셋은 최대 5개까지만 입력할 수 있습니다.")
    private List<Long> skillIds;

    @NotNull(message = "직군을 입력해주세요.")
    private Long occupationId;
}
