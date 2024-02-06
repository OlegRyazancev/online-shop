package com.ryazancev.customer.service.impl;

import com.ryazancev.clients.PurchaseClient;
import com.ryazancev.clients.ReviewClient;
import com.ryazancev.customer.model.Customer;
import com.ryazancev.customer.repository.CustomerRepository;
import com.ryazancev.customer.service.CustomerService;
import com.ryazancev.customer.util.exception.custom.CustomerCreationException;
import com.ryazancev.customer.util.exception.custom.CustomerNotFoundException;
import com.ryazancev.customer.util.mapper.CustomerMapper;
import com.ryazancev.dto.customer.CustomerDTO;
import com.ryazancev.dto.customer.CustomerPurchasesResponse;
import com.ryazancev.dto.customer.UpdateBalanceRequest;
import com.ryazancev.dto.purchase.PurchaseDTO;
import com.ryazancev.dto.purchase.PurchaseEditDTO;
import com.ryazancev.dto.review.ReviewsResponse;
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
    private final ReviewClient reviewClient;


    @Override
    public CustomerDTO getSimpleById(Long id) {

        Customer existing = findById(id);

        return customerMapper.toSimpleDTO(existing);
    }

    @Override
    public CustomerDTO getDetailedById(Long id) {

        Customer existing = findById(id);

        return customerMapper.toDetailedDTO(existing);
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
    public void updateBalance(UpdateBalanceRequest request) {

        Customer existing = findById(request.getCustomerId());
        existing.setBalance(request.getBalance());
        customerRepository.save(existing);
    }

    @Override
    public Double getBalanceByCustomerId(Long id) {

        Customer foundCustomer = findById(id);

        return foundCustomer.getBalance();
    }

    @Transactional
    @Override
    public CustomerDTO create(CustomerDTO customerDTO) {

        if (customerRepository.findByEmail(customerDTO.getEmail()).isPresent()) {
            throw new CustomerCreationException(
                    "Customer with this email already exists",
                    HttpStatus.BAD_REQUEST
            );
        }

        Customer toSave = customerMapper.toEntity(customerDTO);
        Customer saved = customerRepository.save(toSave);

        return customerMapper.toSimpleDTO(saved);
    }

    @Transactional
    @Override
    public CustomerDTO update(CustomerDTO customerDTO) {

        Customer existing = findById(customerDTO.getId());

        existing.setUsername(customerDTO.getUsername());
        existing.setEmail(customerDTO.getEmail());
        existing.setBalance(customerDTO.getBalance());

        Customer updated = customerRepository.save(existing);

        return customerMapper.toDetailedDTO(updated);

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
