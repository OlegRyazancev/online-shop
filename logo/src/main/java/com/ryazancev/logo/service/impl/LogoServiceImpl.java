package com.ryazancev.logo.service.impl;

import com.ryazancev.logo.props.MinioProperties;
import com.ryazancev.logo.service.LogoService;
import com.ryazancev.logo.util.exception.custom.LogoUploadException;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Objects;
import java.util.UUID;


@Slf4j
@Service
@RequiredArgsConstructor
public class LogoServiceImpl implements LogoService {

    private final MinioClient minioClient;
    private final MinioProperties minioProperties;

    @Override
    public String upload(MultipartFile file) {

        try {
            createBucket();
        } catch (Exception e) {
            throw new LogoUploadException(
                    "Logo upload failed " + e.getMessage(),
                    HttpStatus.BAD_REQUEST
            );
        }

        if (file.isEmpty() || file.getOriginalFilename() == null) {
            throw new LogoUploadException(
                    "Logo must have name",
                    HttpStatus.BAD_REQUEST
            );
        }

        String fileName = generateFileName(file);
        InputStream inputStream;

        try {
            inputStream = file.getInputStream();
        } catch (Exception e) {
            throw new LogoUploadException(
                    "Logo upload failed " + e.getMessage(),
                    HttpStatus.BAD_REQUEST
            );
        }

        saveImage(inputStream, fileName);

        return fileName;
    }

    @SneakyThrows
    private void createBucket() {

        boolean found = minioClient.bucketExists(
                BucketExistsArgs.builder()
                        .bucket(minioProperties.getBucket())
                        .build()
        );

        if (!found) {
            minioClient.makeBucket(
                    MakeBucketArgs.builder()
                            .bucket(minioProperties.getBucket())
                            .build()
            );
        }
    }

    private String generateFileName(MultipartFile file) {

        String extension = getExtension(file);

        return UUID.randomUUID() + "." + extension;
    }

    private String getExtension(MultipartFile file) {

        return Objects.requireNonNull(file.getOriginalFilename())
                .substring(file.getOriginalFilename()
                        .lastIndexOf(".") + 1);
    }

    @SneakyThrows
    private void saveImage(InputStream inputStream, String fileName) {

        minioClient.putObject(PutObjectArgs.builder()
                .stream(inputStream, inputStream.available(), -1)
                .bucket(minioProperties.getBucket())
                .object(fileName)
                .build());
    }
}
