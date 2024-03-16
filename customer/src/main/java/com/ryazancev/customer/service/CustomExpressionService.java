package com.ryazancev.customer.service;

/**
 * @author Oleg Ryazancev
 */

public interface CustomExpressionService {

    void checkAccountConditions();

    void checkAccessUser(Long id);

    void checkIfAccountLocked();

    void checkIfCustomerIsAdmin(Long id);

    void checkAccessPrivateNotification(Long customerId, String notificationId);
}
