package com.ryazancev.organization.service;

/**
 * @author Oleg Ryazancev
 */

public interface CustomExpressionService {

    void checkAccountConditions();

    void checkAccessUser(Long customerId);

    void checkAccessOrganization(Long id);

    void checkIfAccountLocked();
}
