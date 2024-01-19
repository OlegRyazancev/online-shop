package com.ryazancev.customer.controller;

import com.ryazancev.clients.customer.CustomerDTO;
import com.ryazancev.clients.customer.CustomerPurchasesResponse;
import com.ryazancev.customer.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<CustomerDTO> getCustomerInfo(@PathVariable("customerId") Long customerId) {
        CustomerDTO customer = customerService.getById(customerId);
        log.info("Request for get customer info {}", customer.toString());
        return ResponseEntity.ok(customer);
    }

    @PutMapping("/{customerId}/increase-balance")
    public ResponseEntity<CustomerDTO> increaseBalance(
            @PathVariable Long customerId,
            @RequestParam Double amount) {
        CustomerDTO customerWithNewBalance = customerService.increaseBalance(customerId, amount);
        log.info("Request to increase customer balance. id {}, amount: {}", customerId, amount);
        return ResponseEntity.ok(customerWithNewBalance);
    }

    @GetMapping("/{customerId}/purchases")
    public ResponseEntity<CustomerPurchasesResponse> getPurchasesByCustomerId(@PathVariable Long customerId) {
        CustomerPurchasesResponse purchases = customerService.getPurchasesByCustomerId(customerId);
        log.info("Request to get purchases by customer id: {}", customerId);
        return ResponseEntity.ok(purchases);
    }


    //todo: add method to watch notifications

}
