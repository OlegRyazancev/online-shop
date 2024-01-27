package com.ryazancev.logo.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

@Data
@Builder
public class Logo implements Serializable {

    private MultipartFile file;
}
