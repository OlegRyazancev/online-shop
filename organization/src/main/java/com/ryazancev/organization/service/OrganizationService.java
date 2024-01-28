package com.ryazancev.organization.service;

import com.ryazancev.clients.logo.dto.LogoDTO;
import com.ryazancev.clients.organization.dto.OrganizationDTO;
import com.ryazancev.clients.organization.dto.OrganizationEditDTO;
import com.ryazancev.clients.organization.dto.OrganizationsSimpleResponse;

public interface OrganizationService {

    OrganizationsSimpleResponse getAll();

    OrganizationDTO getSimpleById(Long id);

    OrganizationDTO getDetailedById(Long id);

    OrganizationDTO register(OrganizationEditDTO organizationEditDTO);

    OrganizationDTO update(OrganizationEditDTO organizationEditDTO);

    void uploadLogo(Long id, LogoDTO logoDTO);
}
