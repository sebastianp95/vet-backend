package com.dev.vetbackend.services;

import com.dev.vetbackend.entity.Asset;
import org.springframework.web.multipart.MultipartFile;

public interface S3Service {
    String putObject(MultipartFile file);

    String getObjectUrl(String key);

    Asset getObject(String key);

    void deleteObject(String key);
}
