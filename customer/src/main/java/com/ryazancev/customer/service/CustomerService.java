package com.ryazancev.customer.service;

import com.ryazancev.clients.customer.dto.CustomerDTO;
import com.ryazancev.clients.customer.dto.CustomerDetailedDTO;

public interface CustomerService {
    CustomerDTO getById(Long id);

    CustomerDetailedDTO getDetailedById(Long id);

    CustomerDetailedDTO updateBalance(Long id, Double amount);
}
