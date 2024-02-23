package com.ryazancev.clients;

import com.ryazancev.config.FeignClientsConfiguration;
import com.ryazancev.dto.customer.CustomerDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "customer",
        configuration = FeignClientsConfiguration.class,
        url = "${clients.customer.url}"
)
public interface CustomerClient {

    @GetMapping("api/v1/customers/{id}/simple")
    CustomerDto getSimpleById(
            @PathVariable("id") Long id);

    @GetMapping("api/v1/customers/{id}/balance")
    Double getBalanceById(
            @PathVariable("id") Long id);

    @PostMapping("api/v1/customers")
    CustomerDto createCustomer(
            @RequestBody CustomerDto customerDto);
}
