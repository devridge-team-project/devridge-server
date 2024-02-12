package org.devridge.api.domain.qna.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FindWriterInformation {

    private Long id;
    private String nickname;
    private String profileImageUrl;
    private String introduction;
}
