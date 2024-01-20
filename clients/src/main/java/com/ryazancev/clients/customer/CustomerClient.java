package com.ryazancev.clients.customer;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "customer")
public interface CustomerClient {

    @GetMapping("api/v1/customers/{customerId}")
    CustomerDTO getInfoById(@PathVariable("customerId") Long customerId);

    @PutMapping("api/v1/customers/{customerId}/update-balance")
    CustomerDTO updateBalance(@PathVariable("customerId") Long customerId, @RequestParam("balance") Double balance);
}
