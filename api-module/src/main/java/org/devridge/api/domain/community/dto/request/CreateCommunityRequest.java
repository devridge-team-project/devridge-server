package org.devridge.api.domain.community.dto.request;

import lombok.Getter;

@Getter
public class CreateCommunityRequest {

    private String title;

    private String content;
}
