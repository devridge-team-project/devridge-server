package org.devridge.api.domain.community.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberInfoResponse {

    @JsonProperty("id")
    private Long memberId;

    @JsonProperty("nickname")
    private String nickName;
    private String profileImageUrl;
    private String introduction;
}
