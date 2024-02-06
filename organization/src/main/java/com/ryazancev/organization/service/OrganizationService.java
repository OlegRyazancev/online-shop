package com.ryazancev.organization.service;

import com.ryazancev.dto.logo.LogoDTO;
import com.ryazancev.dto.organization.OrganizationDTO;
import com.ryazancev.dto.organization.OrganizationEditDTO;
import com.ryazancev.dto.organization.OrganizationsSimpleResponse;
import com.ryazancev.organization.model.OrganizationStatus;

public interface OrganizationService {

    OrganizationsSimpleResponse getAll();

    OrganizationDTO getSimpleById(Long id);

    OrganizationDTO getDetailedById(Long id);

    OrganizationDTO makeRegistrationRequest(OrganizationEditDTO organizationEditDTO);

    OrganizationDTO update(OrganizationEditDTO organizationEditDTO);

    void changeStatusAndRegister(Long organizationId, OrganizationStatus status);

    void uploadLogo(Long id, LogoDTO logoDTO);

    Long getOwnerId(Long organizationId);
}
