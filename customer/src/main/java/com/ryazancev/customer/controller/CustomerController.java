package com.ryazancev.customer.controller;

import com.ryazancev.customer.model.Customer;
import com.ryazancev.customer.service.CustomerService;
import com.ryazancev.customer.service.expression.CustomExpressionService;
import com.ryazancev.customer.util.mapper.CustomerMapper;
import com.ryazancev.dto.customer.CustomerDTO;
import com.ryazancev.dto.customer.CustomerPurchasesResponse;
import com.ryazancev.dto.purchase.PurchaseDTO;
import com.ryazancev.dto.purchase.PurchaseEditDTO;
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


    @GetMapping("/{id}")
    public CustomerDTO getById(
            @PathVariable("id") Long id) {

        customExpressionService.checkIfAccountLocked();
        customExpressionService.checkAccessCustomer(id);

        Customer customer = customerService.getById(id);

        return customerMapper.toDetailedDTO(customer);
    }

    @PutMapping
    public CustomerDTO updateCustomer(
            @RequestBody
            @Validated(OnUpdate.class)
            CustomerDTO customerDTO) {

        customExpressionService.checkIfAccountLocked();
        customExpressionService.checkAccessCustomer(customerDTO.getId());

        Customer customer = customerMapper.toEntity(customerDTO);
        Customer updated = customerService.update(customer);

        return customerMapper.toDetailedDTO(updated);
    }

    @GetMapping("/{id}/reviews")
    public ReviewsResponse getReviewsByCustomerId(
            @PathVariable("id") Long id) {

        customExpressionService.checkIfAccountLocked();
        customExpressionService.checkAccessCustomer(id);

        return customerService.getReviewsByCustomerId(id);
    }

    @GetMapping("/{id}/purchases")
    public CustomerPurchasesResponse getPurchasesByCustomerId(
            @PathVariable("id") Long id) {

        customExpressionService.checkIfAccountLocked();
        customExpressionService.checkAccessCustomer(id);

        return customerService.getPurchasesByCustomerId(id);
    }

    @PostMapping("/purchases")
    public PurchaseDTO processPurchase(
            @RequestBody
            @Validated(OnCreate.class)
            PurchaseEditDTO purchaseEditDTO) {

        customExpressionService.checkIfAccountLocked();
        customExpressionService
                .checkAccessCustomer(purchaseEditDTO.getCustomerId());

        return customerService.processPurchase(purchaseEditDTO);
    }


    //todo: add method to watch notifications

//    Endpoints only  for feign clients

    @GetMapping("/{id}/simple")
    public CustomerDTO getSimpleById(
            @PathVariable("id") Long id) {

        Customer customer = customerService.getById(id);

        return customerMapper.toSimpleDTO(customer);
    }

    @GetMapping("/{id}/balance")
    public Double getBalanceById(
            @PathVariable("id") Long id) {

        return customerService.getBalanceByCustomerId(id);
    }

    @PostMapping
    public CustomerDTO createCustomer(
            @RequestBody
            @Validated(OnCreate.class)
            CustomerDTO customerDTO) {

        Customer customer = customerMapper.toEntity(customerDTO);
        Customer created = customerService.create(customer);

        return customerMapper.toSimpleDTO(created);
    }


}
