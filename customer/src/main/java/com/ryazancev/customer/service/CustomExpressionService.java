package com.ryazancev.customer.service;

public interface CustomExpressionService {

    void checkAccountConditions();

    void checkAccessCustomer(Long id);

    void checkIfAccountLocked();
}
