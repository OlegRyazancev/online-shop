package com.ryazancev.organization.service.expression;

public interface CustomExpressionService {

    boolean canAccessOrganization(Long organizationId);

    boolean canAccessUser(Long userId);
}
