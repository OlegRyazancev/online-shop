package com.ryazancev.customer.service;

import com.ryazancev.clients.customer.CustomerDTO;
import com.ryazancev.clients.customer.CustomerDetailedDTO;

public interface CustomerService {
    CustomerDTO getById(Long customerId);

    CustomerDetailedDTO getDetailedById(Long customerId);

    CustomerDetailedDTO updateBalance(Long customerId, Double amount);
}
