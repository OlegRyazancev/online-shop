package com.ryazancev.customer.service;

import com.ryazancev.clients.customer.CustomerDTO;
import com.ryazancev.clients.customer.CustomerDetailedDTO;

public interface CustomerService {
    CustomerDTO getById(Long id);

    CustomerDetailedDTO getDetailedById(Long id);

    CustomerDetailedDTO updateBalance(Long id, Double amount);
}
