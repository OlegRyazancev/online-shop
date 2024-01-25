package com.ryazancev.clients.customer;

import com.ryazancev.config.FeignClientsConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "customer",
        configuration = FeignClientsConfiguration.class
)
public interface CustomerClient {

    @GetMapping("api/v1/customers/{customerId}")
    CustomerDTO getById(@PathVariable("customerId") Long customerId);

    @GetMapping("api/v1/customers/{customerId}/details")
    CustomerDetailedDTO getDetailedById(@PathVariable("customerId") Long customerId);

    @PutMapping("api/v1/customers/{customerId}/update-balance")
    CustomerDetailedDTO updateBalance(@PathVariable("customerId") Long customerId, @RequestParam("balance") Double balance);
}
