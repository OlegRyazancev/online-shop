package com.ryazancev.organization.service;

import com.ryazancev.clients.organization.OrganizationDetailedDTO;
import com.ryazancev.clients.organization.OrganizationsListResponse;

public interface OrganizationService {

    OrganizationsListResponse getAll();

    OrganizationDetailedDTO getById(Long organizationId);
}
