package com.ryazancev.organization.service;

import com.ryazancev.dto.logo.LogoDTO;
import com.ryazancev.dto.organization.OrganizationDTO;
import com.ryazancev.dto.organization.OrganizationEditDTO;
import com.ryazancev.dto.organization.OrganizationsSimpleResponse;

public interface OrganizationService {

    OrganizationsSimpleResponse getAll();

    OrganizationDTO getSimpleById(Long id);

    OrganizationDTO getDetailedById(Long id);

    OrganizationDTO register(OrganizationEditDTO organizationEditDTO);

    OrganizationDTO update(OrganizationEditDTO organizationEditDTO);

    void uploadLogo(Long id, LogoDTO logoDTO);
}
