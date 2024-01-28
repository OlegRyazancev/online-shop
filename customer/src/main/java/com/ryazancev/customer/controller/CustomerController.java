package com.ryazancev.customer.controller;

import com.ryazancev.clients.customer.dto.CustomerDTO;
import com.ryazancev.clients.customer.dto.CustomerDetailedDTO;
import com.ryazancev.clients.customer.dto.CustomerPurchasesResponse;
import com.ryazancev.clients.purchase.dto.PurchaseDTO;
import com.ryazancev.clients.purchase.dto.PurchasePostDTO;
import com.ryazancev.customer.service.CustomerService;
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

    @GetMapping("/{id}/purchases")
    public CustomerPurchasesResponse getPurchasesByCustomerId(
            @PathVariable("id") Long id) {

        return customerService.getPurchasesByCustomerId(id);
    }

    @PutMapping("/{id}/update-balance")
    public CustomerDetailedDTO updateBalance(
            @PathVariable("id") Long id,
            @RequestParam("balance") Double balance) {

        return customerService.updateBalance(id, balance);
    }

    @PostMapping("/purchases")
    public PurchaseDTO processPurchase(
            @RequestBody
            @Validated PurchasePostDTO purchasePostDTO) {

        return customerService.processPurchase(purchasePostDTO);
    }

    //todo: add method to watch notifications

}
