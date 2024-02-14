package com.ryazancev.organization.service.expression;

import com.ryazancev.dto.organization.OrganizationEditDTO;

public interface CustomExpressionService {

    void checkAccessUser(OrganizationEditDTO organizationEditDTO);

    void checkAccessOrganization(Long id);

    void checkIfAccountLocked();
}
