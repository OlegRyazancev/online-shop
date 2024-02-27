package com.ryazancev.organization.service;

import com.ryazancev.common.dto.organization.OrganizationEditDto;

public interface CustomExpressionService {

    void checkAccountConditions();

    void checkAccessUser(OrganizationEditDto organizationEditDto);

    void checkAccessOrganization(Long id);

    void checkIfAccountLocked();
}
