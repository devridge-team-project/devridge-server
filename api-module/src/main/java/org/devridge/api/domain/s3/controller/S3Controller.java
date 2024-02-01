package org.devridge.api.domain.s3.controller;

import lombok.RequiredArgsConstructor;

import org.devridge.api.domain.s3.dto.response.UploadImageResponse;
import org.devridge.api.domain.s3.service.S3Service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/files")
public class S3Controller {

    private final S3Service s3Service;

    @PostMapping("/images")
    public ResponseEntity<UploadImageResponse> uploadImage(@RequestPart MultipartFile image) throws IOException {
        UploadImageResponse response = s3Service.uploadImage(image);
        return ResponseEntity.ok().body(response);
    }
}
