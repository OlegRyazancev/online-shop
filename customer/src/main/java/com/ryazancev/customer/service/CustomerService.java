package com.ryazancev.customer.service;

import com.ryazancev.common.dto.customer.UpdateBalanceRequest;
import com.ryazancev.customer.model.Customer;

public interface CustomerService {
    Customer getById(Long id);

    Customer update(Customer customer);

    Double getBalanceByCustomerId(Long id);

    Customer updateBalance(UpdateBalanceRequest request);

    Customer create(Customer customer);

    String markCustomerAsDeleted(Long id);
}
