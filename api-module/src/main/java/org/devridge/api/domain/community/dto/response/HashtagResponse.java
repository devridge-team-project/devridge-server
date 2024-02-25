package org.devridge.api.domain.community.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class HashtagResponse {

    private Long id;
    private String word;
    private Long count;
}
