package com.ryazancev.customer.service.expression;

public interface CustomExpressionService {


    void checkIfEmailConfirmed();

    void checkAccessCustomer(Long id);

    void checkIfAccountLocked();
}
