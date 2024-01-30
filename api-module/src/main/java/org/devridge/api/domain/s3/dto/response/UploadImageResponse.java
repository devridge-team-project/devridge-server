package org.devridge.api.domain.s3.dto.response;

import lombok.Getter;

@Getter
public class UploadImageResponse {

    private String imageUrl;

    public UploadImageResponse(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
