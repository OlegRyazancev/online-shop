package com.ryazancev.logo.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author Oleg Ryazancev
 */

public interface LogoService {

    String upload(MultipartFile file);
}
