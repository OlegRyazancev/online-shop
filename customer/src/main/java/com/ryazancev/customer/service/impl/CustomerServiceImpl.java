package com.ryazancev.customer.service.impl;

import com.ryazancev.clients.customer.CustomerDTO;
import com.ryazancev.clients.customer.CustomerPurchasesResponse;
import com.ryazancev.clients.purchase.PurchaseClient;
import com.ryazancev.customer.model.Customer;
import com.ryazancev.customer.repository.CustomerRepository;
import com.ryazancev.customer.service.CustomerService;
import com.ryazancev.customer.util.exception.CustomerNotFoundException;
import com.ryazancev.customer.util.exception.IncorrectBalanceException;
import com.ryazancev.customer.util.mapper.CustomerMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
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
    public CustomerDTO updateBalance(Long customerId, Double balance) {
        if (balance <= 0) {
            throw new IncorrectBalanceException(
                    "Balance must be positive",
                    HttpStatus.BAD_REQUEST
            );
        }
        Customer existing = findCustomerById(customerId);
        existing.setBalance(balance);
        customerRepository.save(existing);
        return customerMapper.toDTO(existing);
    }

    @Override
    public CustomerPurchasesResponse getPurchasesByCustomerId(Long customerId) {
        Customer existing = findCustomerById(customerId);
        return purchaseClient.findByCustomerId(existing.getId());
    }

    private Customer findCustomerById(Long customerId) {
        return customerRepository.findById(customerId)
                .orElseThrow(() ->
                        new CustomerNotFoundException("Customer not found with this ID", HttpStatus.NOT_FOUND));
    }
}
