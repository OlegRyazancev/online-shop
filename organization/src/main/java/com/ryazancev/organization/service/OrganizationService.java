package com.ryazancev.organization.service;

import com.ryazancev.clients.organization.OrganizationDTO;
import com.ryazancev.clients.organization.OrganizationDetailedDTO;
import com.ryazancev.clients.organization.OrganizationsListResponse;
import com.ryazancev.clients.product.ProductDetailedDTO;
import com.ryazancev.clients.product.ProductUpdateDTO;

public interface OrganizationService {

    OrganizationsListResponse getAll();

    OrganizationDTO getById(Long organizationId);

    OrganizationDetailedDTO getDetailedById(Long organizationId);

    ProductDetailedDTO update(Long organizationId, ProductUpdateDTO productUpdateDTO);
}
