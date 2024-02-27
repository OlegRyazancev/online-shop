package com.ryazancev.customer.service.impl;

import com.ryazancev.common.dto.customer.UpdateBalanceRequest;
import com.ryazancev.common.dto.user.UserUpdateRequest;
import com.ryazancev.customer.kafka.CustomerProducerService;
import com.ryazancev.customer.model.Customer;
import com.ryazancev.customer.repository.CustomerRepository;
import com.ryazancev.customer.service.CustomerService;
import com.ryazancev.customer.util.exception.custom.CustomerCreationException;
import com.ryazancev.customer.util.exception.custom.CustomerNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static com.ryazancev.customer.util.exception.Message.CUSTOMER_EMAIL_EXISTS;
import static com.ryazancev.customer.util.exception.Message.CUSTOMER_ID_NOT_FOUND;

/**
 * @author Oleg Ryazancev
 */

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerProducerService customerProducerService;

    @Override
    @Cacheable(
            value = "Customer::getById", key = "#id"
    )
    public Customer getById(Long id) {

        return findById(id);
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

        Customer existing = findById(id);

        return existing.getBalance();
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
                    CUSTOMER_EMAIL_EXISTS,
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

        UserUpdateRequest request = UserUpdateRequest.builder()
                .customerId(customer.getId())
                .email(customer.getEmail())
                .name(customer.getUsername())
                .build();

        customerProducerService.sendMessageToAuthUpdateTopic(request);

        return customerRepository.save(existing);
    }

    @Override
    @Transactional
    @CacheEvict(
            value = "Customer::getById",
            key = "#id"
    )
    public String markCustomerAsDeleted(Long id) {

        Customer existing = findById(id);

        existing.setUsername("DELETED");
        existing.setBalance(0.0);
        existing.setDeletedAt(LocalDateTime.now());

        customerProducerService.sendMessageToAuthDeleteTopic(id);
        customerRepository.save(existing);

        return "Customer was successfully deleted";

    }

    private Customer findById(Long id) {

        return customerRepository.findById(id)
                .orElseThrow(() ->
                        new CustomerNotFoundException(
                                CUSTOMER_ID_NOT_FOUND,
                                HttpStatus.NOT_FOUND
                        ));
    }

}
