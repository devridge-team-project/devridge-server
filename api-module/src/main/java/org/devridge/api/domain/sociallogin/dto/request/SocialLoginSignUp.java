package org.devridge.api.domain.sociallogin.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SocialLoginSignUp {

    @NotBlank(message = "빈 닉네임을 입력할 수 없습니다.")
    @Pattern(regexp = "^[a-zA-Z가-힣 ]*$", message = "잘못된 입력입니다. 다시 입력해주세요.")
    @Size(min = 3, max = 12, message = "닉네임은 (3~12) 글자 입니다.")
    private String nickname;

    @NotBlank(message = "잘못된 입력입니다. 다시 입력해주세요.")
    @Size(min = 5, max = 25, message = "잘못된 입력입니다. 다시 입력해주세요.")
    @Pattern(regexp = "^[a-zA-Z가-힣 ]*$", message = "잘못된 입력입니다. 다시 입력해주세요.")
    private String introduction;

    // TODO: 이미지 처리
    private String profileImageUrl;

    @Size(max = 5, message = "스킬셋은 최대 5개까지만 입력할 수 있습니다.")
    private List<Long> skillIds;

    @NotNull(message = "직군을 입력해주세요.")
    private Long occupationId;

    @NotBlank(message = "tempJwt 값을 입력하세요.")
    private String tempJwt;
}
