package com.ryazancev.organization.service;

import com.ryazancev.dto.Element;
import org.springframework.web.multipart.MultipartFile;

public interface ClientsService {

    Object uploadLogo(MultipartFile multipartFile);

    Object getProductsByOrganizationId(Long id);

    Element getSimpleCustomerById(Long customerId);
}
