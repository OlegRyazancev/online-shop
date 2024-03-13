package com.ryazancev.admin.service;

import com.ryazancev.common.dto.Element;

/**
 * @author Oleg Ryazancev
 */

public interface ClientsService {

    Element getSimpleProductById(Long productId);

    Element getSimpleOrganizationById(Long organizationId);

    Long getOwnerIdByProductId(Long productId);

    Long getOwnerIdByOrganizationId(Long organizationId);
}
