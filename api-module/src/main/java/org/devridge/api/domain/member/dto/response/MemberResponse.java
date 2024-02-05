package org.devridge.api.domain.member.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberResponse {

    private Long id;
    private String nickname;
    private String imageUrl;
    private String introduction;
    private List<Long> skillIds;
    private String occupation;

    @Builder
    public MemberResponse(Long id, String nickname, String imageUrl, String introduction, List<Long> skillIds, String occupation) {
            this.id = id;
            this.nickname = nickname;
            this.imageUrl = imageUrl;
            this.introduction = introduction;
            this.skillIds = skillIds;
            this.occupation = occupation;
    }
}
