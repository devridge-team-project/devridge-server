package org.devridge.api.domain.s3.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import lombok.RequiredArgsConstructor;

import org.devridge.api.domain.s3.dto.response.UploadImageResponse;
import org.devridge.api.exception.common.DataNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class S3Service {

    private final AmazonS3 s3Client;

    @Value("${devridge.s3.bucketName}")
    private String bucketName;

    public UploadImageResponse uploadImage(MultipartFile image, String path) throws IOException {
        String fileName = this.getFileName(image, path);
        ObjectMetadata objectMetadata = this.getObjectMetadata(image);

        PutObjectRequest request = new PutObjectRequest(
            bucketName, fileName, image.getInputStream(), objectMetadata
        );

        s3Client.putObject(request);

        return new UploadImageResponse(fileName);
    }

    public void deleteImage(String name) {
        boolean isExist = s3Client.doesObjectExist(bucketName, name);

        if (isExist) {
            s3Client.deleteObject(bucketName, name);
        } else {
            throw new DataNotFoundException();
        }
    }

    private String getFileName(MultipartFile image, String imagePath) {
        String originalFileName = image.getOriginalFilename();
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));

        return imagePath + "/" + UUID.randomUUID() + extension;
    }

    private ObjectMetadata getObjectMetadata(MultipartFile image) {
        ObjectMetadata objectMetadata = new ObjectMetadata();

        objectMetadata.setContentLength(image.getSize());
        objectMetadata.setContentType(image.getContentType());

        return objectMetadata;
    }
}
