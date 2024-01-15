package com.ryazancev.customer.service.impl;

import com.ryazancev.customer.model.Customer;
import com.ryazancev.customer.repository.CustomerRepository;
import com.ryazancev.customer.service.CustomerService;
import com.ryazancev.customer.util.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    @Override
    public Customer getById(Long customerId) {
        return customerRepository.findById(customerId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Customer not found"));
    }
}
