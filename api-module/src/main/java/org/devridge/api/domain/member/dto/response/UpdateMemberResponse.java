package org.devridge.api.domain.member.dto.response;

import lombok.Getter;

@Getter
public class UpdateMemberResponse {

    private MemberResponse member;

    public UpdateMemberResponse(MemberResponse member) {
        this.member = member;
    }
}
