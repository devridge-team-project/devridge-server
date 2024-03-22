package org.devridge.api.common.dto;

import com.querydsl.core.annotations.QueryProjection;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
public class UserInformation {

    private Long id;
    private String nickname;
    private String profileImageUrl;
    private String introduction;

    @QueryProjection
    public UserInformation(Long id, String nickname, String profileImageUrl, String introduction) {
        this.id = id;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
        this.introduction = introduction;
    }
}
