package com.ryazancev.customer.controller;

import com.ryazancev.clients.customer.CustomerDTO;
import com.ryazancev.clients.customer.CustomerDetailedDTO;
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
    public CustomerDTO getById(@PathVariable("customerId") Long customerId) {
        return customerService.getById(customerId);
    }

    @GetMapping("/{customerId}/details")
    public CustomerDetailedDTO getDetailedById(@PathVariable("customerId") Long customerId) {
        return customerService.getDetailedById(customerId);
    }

    @PutMapping("/{customerId}/update-balance")
    public CustomerDetailedDTO updateBalance(
            @PathVariable("customerId") Long customerId,
            @RequestParam("balance") Double balance) {
        CustomerDetailedDTO customerWithNewBalance = customerService.updateBalance(customerId, balance);
        log.info("Request to increase customer balance. id {}, amount: {}", customerId, balance);
        return customerWithNewBalance;
    }

    //todo: add method to watch notifications

}
