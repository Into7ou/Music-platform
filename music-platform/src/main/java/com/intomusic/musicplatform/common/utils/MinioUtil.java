package com.intomusic.musicplatform.common.utils;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.UUID;

@Slf4j
@Component
public class MinioUtil {

    @Value("${minio.endpoint}")
    private String endpoint;
    @Value("${minio.access-key}")
    private String accessKey;
    @Value("${minio.secret-key}")
    private String secretKey;
    @Value("${minio.bucket-name:music-platform}")
    private String bucketName;

    private MinioClient minioClient;

    @PostConstruct
    public void init() {
        try {
            minioClient = MinioClient.builder()
                    .endpoint(endpoint)
                    .credentials(accessKey, secretKey)
                    .build();
            log.info("MinIO initialized successfully");
        } catch (Exception e) {
            log.error("MinIO init failed", e);
        }
    }

    /**
     * 上传文件并返回访问 URL（MultipartFile 版本）
     */
    public String uploadFile(MultipartFile file, String folder, String contentType) {
        String cleanFolder = "";
        if (folder != null && !folder.trim().isEmpty()) {
            cleanFolder = folder.replaceAll("^/+|/+$", "") + "/";
        }

        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename != null && originalFilename.contains(".")
                ? originalFilename.substring(originalFilename.lastIndexOf("."))
                : "";

        String objectName = cleanFolder + UUID.randomUUID().toString().replace("-", "") + suffix;

        try (InputStream inputStream = file.getInputStream()) {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .stream(inputStream, file.getSize(), -1)
                            .contentType(contentType)
                            .build()
            );

            return endpoint + "/" + bucketName + "/" + objectName;

        } catch (Exception e) {
            log.error("Upload file failed", e);
            throw new RuntimeException("文件上传失败");
        }
    }

    /**
     * 上传文件并返回访问 URL（File 版本 - 新增）
     *
     * @param file        本地文件
     * @param objectName  对象名称（包含路径，如 "music/xxx.mp3"）
     * @param contentType 内容类型
     * @return 文件访问 URL
     */
    public String uploadFile(File file, String objectName, String contentType) {
        try (InputStream inputStream = new FileInputStream(file)) {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .stream(inputStream, file.length(), -1)
                            .contentType(contentType)
                            .build()
            );

            return endpoint + "/" + bucketName + "/" + objectName;

        } catch (Exception e) {
            log.error("Upload file failed", e);
            throw new RuntimeException("文件上传失败");
        }
    }
}