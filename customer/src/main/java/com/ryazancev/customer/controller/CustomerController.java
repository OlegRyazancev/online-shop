package com.ryazancev.customer.controller;

import com.ryazancev.clients.PurchaseClient;
import com.ryazancev.clients.ReviewClient;
import com.ryazancev.customer.model.Customer;
import com.ryazancev.customer.service.CustomerService;
import com.ryazancev.customer.service.expression.CustomExpressionService;
import com.ryazancev.customer.util.exception.custom.ServiceUnavailableException;
import com.ryazancev.customer.util.mapper.CustomerMapper;
import com.ryazancev.dto.customer.CustomerDto;
import com.ryazancev.dto.customer.CustomerPurchasesResponse;
import com.ryazancev.dto.purchase.PurchaseDto;
import com.ryazancev.dto.purchase.PurchaseEditDto;
import com.ryazancev.dto.review.ReviewsResponse;
import com.ryazancev.validation.OnCreate;
import com.ryazancev.validation.OnUpdate;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.ryazancev.customer.util.exception.Messages.PURCHASE_SERVICE_UNAVAILABLE;
import static com.ryazancev.customer.util.exception.Messages.REVIEW_SERVICE_UNAVAILABLE;

@Slf4j
@RestController
@RequestMapping("api/v1/customers")
@RequiredArgsConstructor
@Validated
public class CustomerController {

    private final CustomerService customerService;
    private final CustomExpressionService customExpressionService;
    private final CustomerMapper customerMapper;

    private final PurchaseClient purchaseClient;
    private final ReviewClient reviewClient;


    @GetMapping("/{id}")
    public CustomerDto getById(
            @PathVariable("id") Long id) {

        customExpressionService.checkIfAccountLocked();
        customExpressionService.checkAccessCustomer(id);

        Customer customer = customerService.getById(id);

        return customerMapper.toDetailedDto(customer);
    }

    @PutMapping
    public CustomerDto updateCustomer(
            @RequestBody
            @Validated(OnUpdate.class)
            CustomerDto customerDto) {

        customExpressionService.checkIfEmailConfirmed();
        customExpressionService.checkIfAccountLocked();
        customExpressionService.checkAccessCustomer(customerDto.getId());

        Customer customer = customerMapper.toEntity(customerDto);
        Customer updated = customerService.update(customer);

        return customerMapper.toDetailedDto(updated);
    }

    @GetMapping("/{id}/reviews")
    @CircuitBreaker(
            name = "customer",
            fallbackMethod = "reviewServiceUnavailable"
    )
    public ReviewsResponse getReviewsByCustomerId(
            @PathVariable("id") Long id) {

        customExpressionService.checkIfAccountLocked();
        customExpressionService.checkAccessCustomer(id);

        return reviewClient.getByCustomerId(id);
    }

    @GetMapping("/{id}/purchases")
    @CircuitBreaker(
            name = "customer",
            fallbackMethod = "purchaseServiceUnavailable"
    )
    public CustomerPurchasesResponse getPurchasesByCustomerId(
            @PathVariable("id") Long id) {

        customExpressionService.checkIfAccountLocked();
        customExpressionService.checkAccessCustomer(id);

        return purchaseClient.getByCustomerId(id);
    }

    @PostMapping("/purchases")
    @CircuitBreaker(
            name = "customer",
            fallbackMethod = "purchaseServiceUnavailable"
    )
    public PurchaseDto processPurchase(
            @RequestBody
            @Validated(OnCreate.class)
            PurchaseEditDto purchaseEditDto) {

        customExpressionService.checkIfEmailConfirmed();
        customExpressionService.checkIfAccountLocked();
        customExpressionService
                .checkAccessCustomer(purchaseEditDto.getCustomerId());

        return purchaseClient.processPurchase(purchaseEditDto);
    }

    @DeleteMapping("{id}")
    public String deleteCustomerById(@PathVariable("id") Long id) {

        customExpressionService.checkIfEmailConfirmed();
        customExpressionService.checkIfAccountLocked();
        customExpressionService.checkAccessCustomer(id);

        return customerService.markCustomerAsDeleted(id);
    }

    //todo: add method to watch notifications

//    Endpoints only  for feign clients

    @GetMapping("/{id}/simple")
    public CustomerDto getSimpleById(
            @PathVariable("id") Long id) {

        Customer customer = customerService.getById(id);

        return customerMapper.toSimpleDto(customer);
    }

    @GetMapping("/{id}/balance")
    public Double getBalanceById(
            @PathVariable("id") Long id) {

        return customerService.getBalanceByCustomerId(id);
    }

    @PostMapping
    public CustomerDto createCustomer(
            @RequestBody
            @Validated(OnCreate.class)
            CustomerDto customerDto) {

        Customer customer = customerMapper.toEntity(customerDto);
        Customer created = customerService.create(customer);

        return customerMapper.toSimpleDto(created);
    }

    //Fallback methods

    private ReviewsResponse reviewServiceUnavailable(Exception e) {

        throw new ServiceUnavailableException(
                REVIEW_SERVICE_UNAVAILABLE,
                HttpStatus.SERVICE_UNAVAILABLE);
    }

    private ReviewsResponse purchaseServiceUnavailable(Exception e) {

        throw new ServiceUnavailableException(
                PURCHASE_SERVICE_UNAVAILABLE,
                HttpStatus.SERVICE_UNAVAILABLE);
    }
}
