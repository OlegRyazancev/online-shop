package com.ryazancev.customer.service.impl;

import com.ryazancev.clients.PurchaseClient;
import com.ryazancev.clients.ReviewClient;
import com.ryazancev.customer.model.Customer;
import com.ryazancev.customer.repository.CustomerRepository;
import com.ryazancev.customer.service.CustomerService;
import com.ryazancev.customer.util.exception.custom.CustomerCreationException;
import com.ryazancev.customer.util.exception.custom.CustomerNotFoundException;
import com.ryazancev.dto.customer.CustomerPurchasesResponse;
import com.ryazancev.dto.customer.UpdateBalanceRequest;
import com.ryazancev.dto.purchase.PurchaseDTO;
import com.ryazancev.dto.purchase.PurchaseEditDTO;
import com.ryazancev.dto.review.ReviewsResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    private final PurchaseClient purchaseClient;
    private final ReviewClient reviewClient;


    @Override
    @Cacheable(
            value = "Customer::getById", key = "#id"
    )
    public Customer getById(Long id) {

        return findById(id);
    }

    @Override
    public CustomerPurchasesResponse getPurchasesByCustomerId(Long id) {

        return purchaseClient.getByCustomerId(id);
    }

    @Override
    public PurchaseDTO processPurchase(PurchaseEditDTO purchaseEditDTO) {

        return purchaseClient.processPurchase(purchaseEditDTO);
    }

    @Override
    public ReviewsResponse getReviewsByCustomerId(Long id) {

        return reviewClient.getByCustomerId(id);
    }

    @Transactional
    @Override
    @CachePut(
            value = "Customer::getById",
            key = "#request.customerId"
    )
    public Customer updateBalance(UpdateBalanceRequest request) {

        Customer existing = findById(request.getCustomerId());
        existing.setBalance(request.getBalance());

        return customerRepository.save(existing);
    }

    @Override
    public Double getBalanceByCustomerId(Long id) {

        Customer found = findById(id);

        return found.getBalance();
    }

    @Transactional
    @Override
    @Cacheable(
            value = "Customer::getById",
            condition = "#customer.id!=null",
            key = "#customer.id"
    )
    public Customer create(Customer customer) {

        if (customerRepository.findByEmail(customer.getEmail()).isPresent()) {
            throw new CustomerCreationException(
                    "Customer with this email already exists",
                    HttpStatus.BAD_REQUEST
            );
        }
        customerRepository.save(customer);

        return customer;
    }

    @Transactional
    @Override
    @CachePut(
            value = "Customer::getById",
            key = "#customer.id"
    )
    public Customer update(Customer customer) {

        Customer existing = findById(customer.getId());

        existing.setUsername(customer.getUsername());
        existing.setEmail(customer.getEmail());
        existing.setBalance(customer.getBalance());

        return customerRepository.save(existing);

    }

    private Customer findById(Long id) {

        return customerRepository.findById(id)
                .orElseThrow(() ->
                        new CustomerNotFoundException(
                                "Customer not found with this ID",
                                HttpStatus.NOT_FOUND
                        ));
    }

}
