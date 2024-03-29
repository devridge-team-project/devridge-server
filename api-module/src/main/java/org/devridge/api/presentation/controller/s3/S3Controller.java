package org.devridge.api.presentation.controller.s3;

import lombok.RequiredArgsConstructor;

import org.devridge.api.domain.s3.dto.request.DeleteImageRequest;
import org.devridge.api.domain.s3.dto.response.UploadImageResponse;
import org.devridge.api.application.s3.S3Service;

import org.devridge.api.domain.s3.validator.ValidateImagePath;
import org.devridge.api.domain.s3.type.ImagePath;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/files")
public class S3Controller {

    private final S3Service s3Service;

    @PostMapping("/images")
    public ResponseEntity<UploadImageResponse> uploadImage(
        @RequestPart(value = "image") MultipartFile image,
        @RequestPart(value = "path") @ValidateImagePath(enumClass = ImagePath.class) String path
    ) throws IOException {
        UploadImageResponse response = s3Service.uploadImage(image, path);
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/images")
    public ResponseEntity<Void> deleteImage(@RequestBody @Valid DeleteImageRequest imageRequest) {
        s3Service.deleteImage(imageRequest.getName());
        return ResponseEntity.ok().build();
    }
}
