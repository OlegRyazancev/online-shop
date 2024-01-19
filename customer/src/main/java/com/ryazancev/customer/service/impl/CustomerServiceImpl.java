package com.ryazancev.customer.service.impl;

import com.ryazancev.clients.customer.CustomerDTO;
import com.ryazancev.clients.customer.CustomerPurchasesResponse;
import com.ryazancev.clients.purchase.PurchaseClient;
import com.ryazancev.customer.model.Customer;
import com.ryazancev.customer.repository.CustomerRepository;
import com.ryazancev.customer.service.CustomerService;
import com.ryazancev.customer.util.mappers.CustomerMapper;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    //todo:add custom exceptions using exception service and controllerAdvice
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    private final PurchaseClient purchaseClient;

    @Override
    public CustomerDTO getById(Long customerId) {
        Customer foundCustomer = findCustomerById(customerId);
        return customerMapper.toDTO(foundCustomer);
    }

    @Transactional
    @Override
    public CustomerDTO increaseBalance(Long customerId, Double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        Customer foundCustomer = findCustomerById(customerId);
        Double updatedBalance = foundCustomer.getBalance() + amount;
        foundCustomer.setBalance(updatedBalance);
        customerRepository.save(foundCustomer);
        return customerMapper.toDTO(foundCustomer);
    }

    @Override
    public CustomerPurchasesResponse getPurchasesByCustomerId(Long customerId) {
        return purchaseClient.findByCustomerId(customerId);
    }

    private Customer findCustomerById(Long customerId) {
        return customerRepository.findById(customerId)
                .orElseThrow(() ->
                        new NotFoundException("Customer with not found"));
    }
}
