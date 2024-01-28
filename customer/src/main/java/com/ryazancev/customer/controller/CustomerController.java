package com.ryazancev.customer.controller;

import com.ryazancev.clients.customer.dto.CustomerDTO;
import com.ryazancev.clients.customer.dto.CustomerDetailedDTO;
import com.ryazancev.clients.customer.dto.CustomerPurchasesResponse;
import com.ryazancev.clients.purchase.dto.PurchaseDTO;
import com.ryazancev.clients.purchase.dto.PurchaseEditDTO;
import com.ryazancev.clients.review.dto.ReviewsResponse;
import com.ryazancev.customer.service.CustomerService;
import com.ryazancev.validation.OnCreate;
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
    public CustomerDetailedDTO getDetailedById(
            @PathVariable("id") Long id) {

        return customerService.getDetailedById(id);
    }

    @PutMapping("/{id}/update-balance")
    public CustomerDetailedDTO updateBalance(
            @PathVariable("id") Long id,
            @RequestParam("balance") Double balance) {

        return customerService.updateBalance(id, balance);
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
