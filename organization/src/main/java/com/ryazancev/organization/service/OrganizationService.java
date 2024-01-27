package com.ryazancev.organization.service;

import com.ryazancev.clients.logo.LogoDTO;
import com.ryazancev.clients.organization.*;

public interface OrganizationService {

    OrganizationsListResponse getAll();

    OrganizationSimpleDTO getSimpleById(Long id);

    OrganizationDetailedDTO getDetailedById(Long id);

    OrganizationDetailedDTO register(OrganizationCreateDTO organizationCreateDTO);

    OrganizationDetailedDTO update(OrganizationUpdateDTO organizationUpdateDTO);

    void uploadLogo(Long id, LogoDTO logoDTO);
}
