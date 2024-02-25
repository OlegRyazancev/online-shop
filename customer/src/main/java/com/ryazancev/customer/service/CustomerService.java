package com.ryazancev.customer.service;

import com.ryazancev.customer.model.Customer;
import com.ryazancev.dto.customer.UpdateBalanceRequest;

public interface CustomerService {
    Customer getById(Long id);

    Customer update(Customer customer);

    Double getBalanceByCustomerId(Long id);

    Customer updateBalance(UpdateBalanceRequest request);

    Customer create(Customer customer);

    String markCustomerAsDeleted(Long id);
}
