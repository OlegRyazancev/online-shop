package com.ryazancev.organization.service;

import com.ryazancev.dto.LogoDTO;
import com.ryazancev.dto.OrganizationDTO;
import com.ryazancev.dto.OrganizationEditDTO;
import com.ryazancev.dto.OrganizationsSimpleResponse;

public interface OrganizationService {

    OrganizationsSimpleResponse getAll();

    OrganizationDTO getSimpleById(Long id);

    OrganizationDTO getDetailedById(Long id);

    OrganizationDTO register(OrganizationEditDTO organizationEditDTO);

    OrganizationDTO update(OrganizationEditDTO organizationEditDTO);

    void uploadLogo(Long id, LogoDTO logoDTO);
}
