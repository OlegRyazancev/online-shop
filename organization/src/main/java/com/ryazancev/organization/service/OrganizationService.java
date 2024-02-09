package com.ryazancev.organization.service;

import com.ryazancev.dto.logo.LogoDTO;
import com.ryazancev.organization.model.Organization;
import com.ryazancev.organization.model.OrganizationStatus;

import java.util.List;

public interface OrganizationService {

    List<Organization> getAll();

    Organization getById(Long id);

    Organization makeRegistrationRequest(Organization organization);

    Organization update(Organization organization);

    void changeStatusAndRegister(Long organizationId, OrganizationStatus status);

    void uploadLogo(Long id, LogoDTO logoDTO);

    Long getOwnerId(Long organizationId);
}
