package com.ryazancev.customer.controller;

import com.ryazancev.clients.customer.CustomerDTO;
import com.ryazancev.clients.customer.CustomerPurchasesResponse;
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

    @GetMapping("/{customerId}")
    public CustomerDTO getInfoById(@PathVariable("customerId") Long customerId) {
        CustomerDTO customer = customerService.getById(customerId);
        log.info("Request for get customer info {}", customer.toString());
        return customer;
    }

    @PutMapping("/{customerId}/update-balance")
    public CustomerDTO updateBalance(
            @PathVariable("customerId") Long customerId,
            @RequestParam("balance") Double balance) {
        CustomerDTO customerWithNewBalance = customerService.updateBalance(customerId, balance);
        log.info("Request to increase customer balance. id {}, amount: {}", customerId, balance);
        return customerWithNewBalance;
    }

    @GetMapping("/{customerId}/purchases")
    public CustomerPurchasesResponse getPurchasesByCustomerId(@PathVariable("customerId") Long customerId) {
        CustomerPurchasesResponse purchases = customerService.getPurchasesByCustomerId(customerId);
        log.info("Request to get purchases by customer id: {}", customerId);
        return purchases;
    }


    //todo: add method to watch notifications

}
