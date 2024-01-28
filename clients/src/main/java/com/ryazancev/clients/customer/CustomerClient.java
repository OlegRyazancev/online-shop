package com.ryazancev.clients.customer;

import com.ryazancev.clients.customer.dto.CustomerDTO;
import com.ryazancev.clients.customer.dto.CustomerDetailedDTO;
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

    @GetMapping("api/v1/customers/{id}")
    CustomerDTO getSimpleById(
            @PathVariable("id") Long id);


    @GetMapping("api/v1/customers/{id}/details")
    CustomerDetailedDTO getDetailedById(
            @PathVariable("id") Long id);

    @PutMapping("api/v1/customers/{id}/update-balance")
    CustomerDetailedDTO updateBalance(
            @PathVariable("id") Long id,
            @RequestParam("balance") Double balance);
}
