package com.ryazancev.clients.customer;

import com.ryazancev.clients.customer.dto.CustomerDTO;
import com.ryazancev.config.FeignClientsConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(
        name = "customer",
        configuration = FeignClientsConfiguration.class
)
public interface CustomerClient {

    @GetMapping("api/v1/customers/{id}")
    CustomerDTO getSimpleById(
            @PathVariable("id") Long id);


    @GetMapping("api/v1/customers/{id}/details")
    CustomerDTO getDetailedById(
            @PathVariable("id") Long id);

    @PutMapping("api/v1/customers/{id}/update-balance")
    CustomerDTO updateBalance(
            @PathVariable("id") Long id,
            @RequestParam("balance") Double balance);

    @PostMapping("api/v1/customers")
    CustomerDTO createCustomer(
            @RequestBody CustomerDTO customerDTO);
}
