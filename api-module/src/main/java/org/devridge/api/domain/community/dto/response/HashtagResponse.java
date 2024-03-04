package org.devridge.api.domain.community.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class HashtagResponse {

    private Long id;
    private String word;
}
