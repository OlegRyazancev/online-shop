package com.ryazancev.customer.controller;

import com.ryazancev.customer.model.Customer;
import com.ryazancev.customer.service.CustomerService;
import com.ryazancev.customer.service.clients.ClientsService;
import com.ryazancev.customer.service.expression.CustomExpressionService;
import com.ryazancev.customer.util.mapper.CustomerMapper;
import com.ryazancev.dto.customer.CustomerDto;
import com.ryazancev.dto.customer.CustomerPurchasesResponse;
import com.ryazancev.dto.purchase.PurchaseDto;
import com.ryazancev.dto.purchase.PurchaseEditDto;
import com.ryazancev.dto.review.ReviewsResponse;
import com.ryazancev.validation.OnCreate;
import com.ryazancev.validation.OnUpdate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("api/v1/customers")
@RequiredArgsConstructor
@Validated
public class CustomerController {

    private final CustomerService customerService;
    private final CustomExpressionService customExpressionService;
    private final CustomerMapper customerMapper;

    private final ClientsService clientsService;

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
    public ReviewsResponse getReviewsByCustomerId(
            @PathVariable("id") Long id) {

        customExpressionService.checkIfAccountLocked();
        customExpressionService.checkAccessCustomer(id);

        return (ReviewsResponse) clientsService
                .getReviewsByCustomerId(id);
    }

    @GetMapping("/{id}/purchases")
    public CustomerPurchasesResponse getPurchasesByCustomerId(
            @PathVariable("id") Long id) {

        customExpressionService.checkIfAccountLocked();
        customExpressionService.checkAccessCustomer(id);

        return (CustomerPurchasesResponse) clientsService
                .getPurchasesByCustomerId(id);
    }

    @PostMapping("/purchases")
    public PurchaseDto processPurchase(
            @RequestBody
            @Validated(OnCreate.class)
            PurchaseEditDto purchaseEditDto) {

        customExpressionService.checkIfEmailConfirmed();
        customExpressionService.checkIfAccountLocked();
        customExpressionService
                .checkAccessCustomer(purchaseEditDto.getCustomerId());

        return (PurchaseDto) clientsService
                .processPurchase(purchaseEditDto);
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
}
