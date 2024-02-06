package com.ryazancev.customer.controller;

import com.ryazancev.customer.service.CustomExpressionService;
import com.ryazancev.customer.service.CustomerService;
import com.ryazancev.customer.util.exception.custom.AccessDeniedException;
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


    @GetMapping("/{id}/simple")
    public CustomerDTO getSimpleById(
            @PathVariable("id") Long id) {

        return customerService.getSimpleById(id);
    }

    @GetMapping("/{id}")
    public CustomerDTO getById(
            @PathVariable("id") Long id) {

        checkAccessCustomer(id);

        return customerService.getDetailedById(id);
    }

    @PostMapping
    public CustomerDTO createCustomer(
            @RequestBody
            @Validated(OnCreate.class)
            CustomerDTO customerDTO) {

        return customerService.create(customerDTO);
    }

    @PutMapping
    public CustomerDTO updateCustomer(
            @RequestBody
            @Validated(OnUpdate.class)
            CustomerDTO customerDTO) {

        checkAccessCustomer(customerDTO.getId());

        return customerService.update(customerDTO);
    }

    @GetMapping("/{id}/reviews")
    public ReviewsResponse getReviewsByCustomerId(
            @PathVariable("id") Long id) {

        checkAccessCustomer(id);

        return customerService.getReviewsByCustomerId(id);
    }

    @GetMapping("/{id}/purchases")
    public CustomerPurchasesResponse getPurchasesByCustomerId(
            @PathVariable("id") Long id) {

        checkAccessCustomer(id);

        return customerService.getPurchasesByCustomerId(id);
    }

    @PostMapping("/purchases")
    public PurchaseDTO processPurchase(
            @RequestBody
            @Validated(OnCreate.class)
            PurchaseEditDTO purchaseEditDTO) {

        checkAccessCustomer(purchaseEditDTO.getCustomerId());

        return customerService.processPurchase(purchaseEditDTO);
    }

    @GetMapping("/{id}/balance")
    public Double getBalanceByCustomerId(
            @PathVariable("id") Long id){

        return customerService.getBalanceByCustomerId(id);
    }

    private void checkAccessCustomer(Long customerId) {

        if (!customExpressionService.canAccessCustomer(customerId)) {
            throw new AccessDeniedException();
        }
    }


    //todo: add method to watch notifications

}
