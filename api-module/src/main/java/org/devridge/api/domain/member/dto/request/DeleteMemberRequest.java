package org.devridge.api.domain.member.dto.request;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class DeleteMemberRequest {

    @NotBlank(message = "빈 비밀번호를 입력할 수 없습니다.")
    private String password;
}
