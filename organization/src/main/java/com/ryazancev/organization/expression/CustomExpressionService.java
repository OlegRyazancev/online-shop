package com.ryazancev.organization.expression;

public interface CustomExpressionService {

    boolean canAccessOrganization(Long organizationId);

    boolean canAccessUser(Long userId);
}
