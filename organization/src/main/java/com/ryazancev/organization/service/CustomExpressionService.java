package com.ryazancev.organization.service;

import com.ryazancev.dto.organization.OrganizationEditDto;

public interface CustomExpressionService {

    void checkAccessUser(OrganizationEditDto organizationEditDto);

    void checkAccessOrganization(Long id);

    void checkIfAccountLocked();

    void checkIfEmailConfirmed();
}