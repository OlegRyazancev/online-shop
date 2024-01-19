package com.ryazancev.customer.service;

import com.ryazancev.clients.customer.CustomerDTO;
import com.ryazancev.clients.customer.CustomerPurchasesResponse;

public interface CustomerService {
    CustomerDTO getById(Long customerId);

    CustomerDTO increaseBalance(Long customerId, Double amount);

    CustomerPurchasesResponse getPurchasesByCustomerId(Long customerId);
}
