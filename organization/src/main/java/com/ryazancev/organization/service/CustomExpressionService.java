package com.ryazancev.organization.service;

public interface CustomExpressionService {

    boolean canAccessOrganization(Long organizationId);

    boolean canAccessUser(Long userId);
}
