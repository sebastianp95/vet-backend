package com.dev.vetbackend.services.impl;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import com.dev.vetbackend.entity.Asset;
import com.dev.vetbackend.services.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.UUID;

@Service
public class S3ServiceImpl implements S3Service {

    @Value("${aws.s3.bucket.name}")
    private String bucketName;

    @Autowired
    private AmazonS3 s3Client;

    @Override
    public String putObject(MultipartFile file) {
        String extension = StringUtils.getFilenameExtension(file.getOriginalFilename());
        String key = String.format("%s.%s", UUID.randomUUID(), extension);

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());

        try {
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, file.getInputStream(), metadata);
//            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, file.getInputStream(), metadata)
//                    .withCannedAcl(CannedAccessControlList.PublicRead);
            s3Client.putObject(putObjectRequest);
            return key;
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload file to S3", e);
        }
    }

    @Override
    public Asset getObject(String key) {
        S3Object s3Object = s3Client.getObject(bucketName, key);
        ObjectMetadata objectMetadata = s3Object.getObjectMetadata();
        try {
            S3ObjectInputStream objectContent = s3Object.getObjectContent();
            byte[] bytes = IOUtils.toByteArray(objectContent);

            return new Asset(bytes, objectMetadata.getContentType());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteObject(String key) {
        s3Client.deleteObject(bucketName, key);
    }

    @Override
    public String getObjectUrl(String key) {
        return String.format("https://%s.s3.amazonaws.com/%s", bucketName, key);
    }
}
