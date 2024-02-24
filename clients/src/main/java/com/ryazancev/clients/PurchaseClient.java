package com.ryazancev.clients;

import com.ryazancev.config.FeignClientsConfiguration;
import com.ryazancev.dto.customer.CustomerPurchasesResponse;
import com.ryazancev.dto.purchase.PurchaseDto;
import com.ryazancev.dto.purchase.PurchaseEditDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "purchase",
        configuration = FeignClientsConfiguration.class,
        url = "${clients.purchase.url}"
)
public interface PurchaseClient {

    @GetMapping("api/v1/purchases/customer/{id}")
    CustomerPurchasesResponse getByCustomerId(
            @PathVariable("id") Long id);

    @PostMapping("api/v1/purchases")
    PurchaseDto processPurchase(
            @RequestBody PurchaseEditDto purchaseEditDto);

    @GetMapping("api/v1/purchases/{id}")
    PurchaseDto getById(
            @PathVariable("id") String id);
}
