package com.ryazancev.clients;

import com.ryazancev.config.FeignClientsConfiguration;
import com.ryazancev.dto.customer.CustomerPurchasesResponse;
import com.ryazancev.dto.purchase.PurchaseDTO;
import com.ryazancev.dto.purchase.PurchaseEditDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "purchase",
        configuration = FeignClientsConfiguration.class
)
public interface PurchaseClient {

    @GetMapping("api/v1/purchases/customer/{id}")
    CustomerPurchasesResponse getByCustomerId(
            @PathVariable("id") Long id);

    @PostMapping("api/v1/purchases")
    PurchaseDTO processPurchase(
            @RequestBody PurchaseEditDTO purchaseEditDTO);
}
