package com.ryazancev.customer.service.impl;

import com.ryazancev.common.dto.customer.UpdateBalanceRequest;
import com.ryazancev.customer.model.Customer;
import com.ryazancev.customer.repository.CustomerRepository;
import com.ryazancev.customer.service.CustomerService;
import com.ryazancev.customer.util.exception.CustomExceptionFactory;
import com.ryazancev.customer.util.processor.KafkaMessageProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Locale;


/**
 * @author Oleg Ryazancev
 */

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    private final KafkaMessageProcessor kafkaMessageProcessor;
    private final MessageSource messageSource;

    @Override
    @Cacheable(
            value = "Customer::getById", key = "#id"
    )
    public Customer getById(final Long id) {

        return findById(id);
    }

    @Transactional
    @Override
    @CachePut(
            value = "Customer::getById",
            key = "#request.customerId"
    )
    public Customer updateBalance(final UpdateBalanceRequest request) {

        Customer existing = findById(request.getCustomerId());
        existing.setBalance(request.getBalance());

        return customerRepository.save(existing);
    }

    @Override
    public Double getBalanceByCustomerId(final Long id) {

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
    public Customer create(final Customer customer) {

        if (customerRepository.findByEmail(customer.getEmail()).isPresent()) {

            throw CustomExceptionFactory
                    .getCustomerCreation()
                    .emailExists();
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
    public Customer update(final Customer customer) {

        Customer existing = findById(customer.getId());

        existing.setUsername(customer.getUsername());
        existing.setEmail(customer.getEmail());
        existing.setBalance(customer.getBalance());

        kafkaMessageProcessor.sendUpdateUserRequestToAuthUpdateTopic(existing);

        return customerRepository.save(existing);
    }

    @Override
    @Transactional
    @CacheEvict(
            value = "Customer::getById",
            key = "#id"
    )
    public String markCustomerAsDeleted(final Long id) {

        Customer existing = findById(id);

        existing.setUsername("DELETED");
        existing.setBalance(0.0);
        existing.setDeletedAt(LocalDateTime.now());

        customerRepository.save(existing);

        kafkaMessageProcessor.sendCustomerIdToAuthDeleteTopic(id);

        return messageSource.getMessage(
                "service.customer.deleted",
                new Object[]{id},
                Locale.getDefault()
        );
    }

    private Customer findById(final Long id) {

        return customerRepository.findById(id)
                .orElseThrow(() ->
                        CustomExceptionFactory
                                .getCustomerNotFound()
                                .byId(String.valueOf(id))
                );
    }
}
