package org.devridge.common.dto;

import com.querydsl.core.annotations.QueryProjection;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FindWriterInformation {

    private Long id;
    private String nickname;
    private String profileImageUrl;
    private String introduction;

    @QueryProjection
    public FindWriterInformation(Long id, String nickname, String profileImageUrl, String introduction) {
        this.id = id;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
        this.introduction = introduction;
    }
}