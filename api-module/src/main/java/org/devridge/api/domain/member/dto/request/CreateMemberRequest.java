package org.devridge.api.domain.member.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CreateMemberRequest {

    @NotBlank(message = "빈 이메일을 입력할 수 없습니다.")
    private String email;

    private String password;

    private String provider = "normal";

    @NotBlank(message = "빈 닉네임을 입력할 수 없습니다.")
    private String nickname;

    private String profileImageUrl;

    private String skills;

}
