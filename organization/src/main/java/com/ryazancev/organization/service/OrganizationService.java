package com.ryazancev.organization.service;

import com.ryazancev.clients.logo.LogoDTO;
import com.ryazancev.clients.organization.*;

public interface OrganizationService {

    OrganizationsListResponse getAll();

    OrganizationDTO getById(Long organizationId);

    OrganizationDetailedDTO getDetailedById(Long organizationId);

    OrganizationDetailedDTO register(OrganizationCreateDTO organizationCreateDTO);

    OrganizationDetailedDTO update(OrganizationUpdateDTO organizationUpdateDTO);

    void uploadLogo(Long organizationId, LogoDTO logoDTO);
}
