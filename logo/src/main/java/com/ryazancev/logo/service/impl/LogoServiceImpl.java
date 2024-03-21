package com.ryazancev.logo.service.impl;

import com.ryazancev.logo.props.MinioProperties;
import com.ryazancev.logo.service.LogoService;
import com.ryazancev.logo.util.exception.CustomExceptionFactory;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Objects;
import java.util.UUID;

/**
 * @author Oleg Ryazancev
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class LogoServiceImpl implements LogoService {

    private final MinioClient minioClient;
    private final MinioProperties minioProperties;
    private final MessageSource messageSource;

    @Override
    public String upload(final MultipartFile file) {

        try {
            createBucket();
        } catch (Exception e) {

            throw CustomExceptionFactory
                    .getLogoUpload()
                    .failed(
                            messageSource,
                            e.getMessage()
                    );
        }

        if (file.isEmpty() || file.getOriginalFilename() == null) {

            throw CustomExceptionFactory
                    .getLogoUpload()
                    .mustHaveName(messageSource);
        }

        String fileName = generateFileName(file);
        InputStream inputStream;

        try {
            inputStream = file.getInputStream();
        } catch (Exception e) {

            throw CustomExceptionFactory
                    .getLogoUpload()
                    .failed(
                            messageSource,
                            e.getMessage()
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

    private String generateFileName(final MultipartFile file) {

        String extension = getExtension(file);

        return UUID.randomUUID() + "." + extension;
    }

    private String getExtension(final MultipartFile file) {

        return Objects.requireNonNull(file.getOriginalFilename())
                .substring(file.getOriginalFilename()
                        .lastIndexOf(".") + 1);
    }

    @SneakyThrows
    private void saveImage(final InputStream inputStream,
                           final String fileName) {

        minioClient.putObject(PutObjectArgs.builder()
                .stream(inputStream, inputStream.available(), -1)
                .bucket(minioProperties.getBucket())
                .object(fileName)
                .build());
    }
}
