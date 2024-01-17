package com.ryazancev.customer.service.impl;

import com.ryazancev.customer.dto.CustomerPurchasesResponse;
import com.ryazancev.customer.model.Customer;
import com.ryazancev.customer.repository.CustomerRepository;
import com.ryazancev.customer.service.CustomerService;
import com.ryazancev.customer.util.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final RestTemplate restTemplate;

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
        CustomerPurchasesResponse customerPurchases = restTemplate.getForObject(
                "http://localhost:8082/api/v1/purchases/customer/{customerId}",
                CustomerPurchasesResponse.class,
                customerId
        );
        if (customerPurchases != null) {
            log.info("Receive customer purchases: {}", customerPurchases);
            return customerPurchases;
        }
        log.info("Customer purchases is null");
        return null;
    }
}
