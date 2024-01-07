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

    @NotBlank(message = "빈 이메일을 입력할 수 없습니다.")
    private String email;

    private String password;

    private String provider = MemberConstant.PROVIDER_NORMAL;

    @NotBlank(message = "빈 닉네임을 입력할 수 없습니다.")
    private String nickname;

    private String profileImageUrl;

    private String skillSet;

    public String getSillSet(){
        return skillSet != null ? skillSet.toLowerCase().trim() : null;
    }

    @Override
    public String toString() {
        return "CreateMemberRequest{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", provider='" + provider + '\'' +
                ", nickname='" + nickname + '\'' +
                ", profileImageUrl='" + profileImageUrl + '\'' +
                ", skillSet='" + skillSet + '\'' +
                '}';
    }
}
