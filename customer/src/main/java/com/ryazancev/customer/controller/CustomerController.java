package com.ryazancev.customer.controller;

import com.ryazancev.customer.service.CustomerService;
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


    @GetMapping("/{id}")
    public CustomerDTO getSimpleById(
            @PathVariable("id") Long id) {

        return customerService.getById(id);
    }

    @GetMapping("/{id}/details")
    public CustomerDTO getDetailedById(
            @PathVariable("id") Long id) {

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

        return customerService.update(customerDTO);
    }

    @GetMapping("/{id}/reviews")
    public ReviewsResponse getReviewsByCustomerId(
            @PathVariable("id") Long id) {

        return customerService.getReviewsByCustomerId(id);
    }

    @GetMapping("/{id}/purchases")
    public CustomerPurchasesResponse getPurchasesByCustomerId(
            @PathVariable("id") Long id) {

        return customerService.getPurchasesByCustomerId(id);
    }

    @PostMapping("/purchases")
    public PurchaseDTO processPurchase(
            @RequestBody
            @Validated(OnCreate.class)
            PurchaseEditDTO purchaseEditDTO) {

        return customerService.processPurchase(purchaseEditDTO);
    }


    //todo: add method to watch notifications

}
