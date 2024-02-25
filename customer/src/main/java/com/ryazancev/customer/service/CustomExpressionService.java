package com.ryazancev.customer.service;

public interface CustomExpressionService {


    void checkIfEmailConfirmed();

    void checkAccessCustomer(Long id);

    void checkIfAccountLocked();
}
