package org.devridge.api.domain.member.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devridge.api.constant.MemberConstant;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CreateMemberRequest {

    @NotBlank(message = "빈 이메일을 보낼 수 없습니다.")
    private String email;

    @NotBlank(message = "빈 비밀번호를 입력할 수 없습니다.")
    private String password;

    private String provider = MemberConstant.PROVIDER_NORMAL;

    @NotBlank(message = "빈 닉네임을 입력할 수 없습니다.")
    private String nickname;

    private String profileImageUrl;

    private String skillSet;
}
