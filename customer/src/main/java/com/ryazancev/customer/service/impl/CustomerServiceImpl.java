package com.ryazancev.customer.service.impl;

import com.ryazancev.clients.customer.dto.CustomerDTO;
import com.ryazancev.clients.customer.dto.CustomerPurchasesResponse;
import com.ryazancev.clients.purchase.PurchaseClient;
import com.ryazancev.clients.purchase.dto.PurchaseDTO;
import com.ryazancev.clients.purchase.dto.PurchaseEditDTO;
import com.ryazancev.clients.review.ReviewClient;
import com.ryazancev.clients.review.dto.ReviewsResponse;
import com.ryazancev.customer.model.Customer;
import com.ryazancev.customer.repository.CustomerRepository;
import com.ryazancev.customer.service.CustomerService;
import com.ryazancev.customer.util.exception.custom.CustomerCreationException;
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

    private final PurchaseClient purchaseClient;
    private final ReviewClient reviewClient;

    @Override
    public CustomerDTO getById(Long id) {

        Customer existing = findById(id);

        return customerMapper.toSimple(existing);
    }

    @Override
    public CustomerDTO getDetailedById(Long id) {

        Customer existing = findById(id);

        return customerMapper.toDetailedDTO(existing);
    }

    @Transactional
    @Override
    public CustomerDTO updateBalance(Long id, Double balance) {

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
    public CustomerDTO create(CustomerDTO customerDTO) {
        if (customerRepository.findByEmail(customerDTO.getEmail()).isPresent()) {
            throw new CustomerCreationException(
                    "Customer with this email already exists",
                    HttpStatus.BAD_REQUEST
            );
        }

        Customer toSave = customerMapper.toEntity(customerDTO);
        Customer saved = customerRepository.save(toSave);

        return customerMapper.toSimple(saved);
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
