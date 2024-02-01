package org.devridge.api.domain.s3.dto.response;

import lombok.Getter;

@Getter
public class UploadImageResponse {

    private String imagePath;

    public UploadImageResponse(String imagePath) {
        this.imagePath = imagePath;
    }
}
