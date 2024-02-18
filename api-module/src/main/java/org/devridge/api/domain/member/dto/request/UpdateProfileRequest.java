package org.devridge.api.domain.member.dto.request;

import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
public class UpdateProfileRequest {

    @NotBlank(message = "자기소개를 입력해주세요.")
    @Size(min = 5, max = 25, message = "자기소개는 5 ~ 25자내로 입력해주세요.")
    @Pattern(regexp = "^[a-zA-Z가-힣0-9,.! ]*$", message = "자기소개는 영어, 한글, 숫자, 그리고 특수문자(, . ! ?)만 입력 가능합니다.")
    private String introduction;

    private List<Long> skillIds;
}
