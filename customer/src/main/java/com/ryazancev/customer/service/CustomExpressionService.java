package com.ryazancev.customer.service;

/**
 * @author Oleg Ryazancev
 */

public interface CustomExpressionService {

    void checkAccountConditions();

    void checkAccessCustomer(Long id);

    void checkIfAccountLocked();
}
