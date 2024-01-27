package com.ryazancev.customer.service.impl;

import com.ryazancev.clients.customer.CustomerDTO;
import com.ryazancev.clients.customer.CustomerDetailedDTO;
import com.ryazancev.customer.model.Customer;
import com.ryazancev.customer.repository.CustomerRepository;
import com.ryazancev.customer.service.CustomerService;
import com.ryazancev.customer.util.exception.custom.CustomerNotFoundException;
import com.ryazancev.customer.util.exception.custom.IncorrectBalanceException;
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

    @Override
    public CustomerDTO getById(Long id) {

        Customer foundCustomer = findById(id);

        return customerMapper.toDTO(foundCustomer);
    }

    @Override
    public CustomerDetailedDTO getDetailedById(Long id) {

        Customer foundCustomer = findById(id);

        return customerMapper.toDetailedDTO(foundCustomer);
    }

    @Transactional
    @Override
    public CustomerDetailedDTO updateBalance(Long id, Double balance) {

        if (balance <= 0) {
            throw new IncorrectBalanceException(
                    "Balance must be positive",
                    HttpStatus.BAD_REQUEST
            );
        }
        Customer existing = findById(id);
        existing.setBalance(balance);
        customerRepository.save(existing);

        return customerMapper.toDetailedDTO(existing);
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
