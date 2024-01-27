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

    //todo: add method to watch notifications

}
