package org.devridge.api.domain.member.dto.request;

import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
public class UpdateMemberProfileRequest {

    // TODO: 이미지 처리
    private String profileImageUrl;

    @NotBlank(message = "잘못된 입력입니다. 다시 입력해주세요.")
    @Size(min = 5, max = 25, message = "잘못된 입력입니다. 다시 입력해주세요.")
    @Pattern(regexp = "^[a-zA-Z가-힣 ]*$", message = "잘못된 입력입니다. 다시 입력해주세요.")
    private String introduction;

    private List<Long> skillIds;
}
