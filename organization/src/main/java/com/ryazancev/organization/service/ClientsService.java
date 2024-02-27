package com.ryazancev.organization.service;

import com.ryazancev.common.dto.Element;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Oleg Ryazancev
 */

public interface ClientsService {

    Object uploadLogo(MultipartFile multipartFile);

    Object getProductsByOrganizationId(Long id);

    Element getSimpleCustomerById(Long customerId);
}
