package com.ryazancev.organization.service;

import com.ryazancev.dto.logo.LogoDto;
import com.ryazancev.organization.model.Organization;
import com.ryazancev.organization.model.OrganizationStatus;

import java.util.List;

public interface OrganizationService {

    List<Organization> getAll();

    Organization getById(Long id, boolean statusCheck);

    Organization makeRegistrationRequest(Organization organization);

    Organization update(Organization organization);

    void changeStatus(Long organizationId, OrganizationStatus status);

    void register(Long organizationId);

    String uploadLogo(Long id, LogoDto logoDto);

    Long getOwnerId(Long organizationId);

    String markOrganizationAsDeleted(Long id);
}
