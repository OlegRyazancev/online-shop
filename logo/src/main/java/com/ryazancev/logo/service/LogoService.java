package com.ryazancev.logo.service;

import org.springframework.web.multipart.MultipartFile;


public interface LogoService {

    String upload(MultipartFile file);
}
