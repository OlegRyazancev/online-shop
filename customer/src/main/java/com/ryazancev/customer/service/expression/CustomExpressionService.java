package com.ryazancev.customer.service.expression;

public interface CustomExpressionService {

    void checkAccessCustomer(Long id);

    void checkIfAccountLocked();
}
