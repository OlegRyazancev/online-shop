package com.ryazancev.customer.service;

import com.ryazancev.customer.model.Customer;

public interface CustomerService {
    Customer getById(Long customerId);

    String increaseBalance(Long customerId, Double amount);
}
