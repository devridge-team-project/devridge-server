package org.devridge.api.domain.member.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CreateMemberRequest {

    @NotBlank(message = "빈 이메일을 입력할 수 없습니다.")
    @Email(message = "유효하지 않은 이메일 형식입니다.")
    private String email;

    @NotBlank(message = "빈 패스워드를 입력할 수 없습니다.")
    @Size(min = 8, max = 20, message = "패스워드는 8자 이상 20자 이하이어야 합니다.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,20}$", message = "패스워드는 영문자 + 숫자 조합이어야 합니다.")
    private String password;

    @NotBlank(message = "빈 provider를 입력할 수 없습니다.")
    private String provider;

    @NotBlank(message = "빈 닉네임을 입력할 수 없습니다.")
    private String nickname;

    private String introduction;

    private List<Long> occupationIds;

    // TODO: 이미지 처리
    private String profileImageUrl;

    private List<Long> skillIds;
}
