package org.devridge.api.domain.member.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CreateMemberRequest {

    // TODO: 이메일 검증 로직 필요
    @NotBlank(message = "빈 이메일을 입력할 수 없습니다.")
    private String email;

    @NotBlank(message = "빈 패스워드를 입력할 수 없습니다.")
    private String password;

    @NotBlank(message = "빈 provider를 입력할 수 없습니다.")
    private String provider;

    @NotBlank(message = "빈 닉네임을 입력할 수 없습니다.")
    private String nickname;

    private String profileImageUrl;

    private String skills;

}
