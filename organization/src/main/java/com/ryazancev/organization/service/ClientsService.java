package com.ryazancev.organization.service;

import org.springframework.web.multipart.MultipartFile;

public interface ClientsService {

    Object uploadLogo(MultipartFile multipartFile);

    Object getProductsByOrganizationId(Long id);

    Object getSimpleCustomerById(Long customerId);
}
