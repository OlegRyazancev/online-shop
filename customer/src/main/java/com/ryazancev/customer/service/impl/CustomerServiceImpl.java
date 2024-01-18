package com.ryazancev.customer.service.impl;

import com.ryazancev.clients.PurchaseClient;
import com.ryazancev.clients.CustomerPurchasesResponse;
import com.ryazancev.customer.model.Customer;
import com.ryazancev.customer.repository.CustomerRepository;
import com.ryazancev.customer.service.CustomerService;
import com.ryazancev.customer.util.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final PurchaseClient purchaseClient;

    @Override
    public Customer getById(Long customerId) {
        return customerRepository.findById(customerId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Customer not found"));
    }

    @Transactional
    @Override
    public String increaseBalance(Long customerId, Double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        Customer customer = getById(customerId);
        Double updatedBalance = customer.getBalance() + amount;
        customer.setBalance(updatedBalance);
        customerRepository.save(customer);
        return "The customer's balance has been successfully increased!";
    }

    @Override
    public CustomerPurchasesResponse getPurchasesByCustomerId(Long customerId) {

        CustomerPurchasesResponse customerPurchases =
                purchaseClient.findByCustomerId(customerId);

        if (customerPurchases != null) {
            log.info("Receive customer purchases: {}", customerPurchases);
            return customerPurchases;
        }
        log.info("Customer purchases is null");
        return null;
    }
}
