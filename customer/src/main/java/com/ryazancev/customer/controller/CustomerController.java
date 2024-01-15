package com.ryazancev.customer.controller;

import com.ryazancev.customer.dto.CustomerDTO;
import com.ryazancev.customer.model.Customer;
import com.ryazancev.customer.service.CustomerService;
import com.ryazancev.customer.util.mappers.CustomerMapper;
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
    private final CustomerMapper customerMapper;

    @GetMapping("/{customerId}")
    public ResponseEntity<CustomerDTO> getCustomerInfo(@PathVariable("customerId") Long customerId) {
        Customer customer = customerService.getById(customerId);
        log.info("Request for get customer info {}", customer.toString());
        return ResponseEntity.ok(customerMapper.toDTO(customer));
    }

    //todo: add method to watch purchase history
    //todo: add method to increase user's balance
    //todo: add method to watch notifications????

}
