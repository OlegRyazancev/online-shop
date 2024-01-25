package com.ryazancev.customer.service;

import com.ryazancev.clients.customer.CustomerDTO;

public interface CustomerService {
    CustomerDTO getById(Long customerId);

    CustomerDTO updateBalance(Long customerId, Double amount);

}
