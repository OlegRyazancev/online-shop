package com.ryazancev.clients.purchase;

import com.ryazancev.clients.customer.dto.CustomerPurchasesResponse;
import com.ryazancev.clients.purchase.dto.PurchaseDTO;
import com.ryazancev.clients.purchase.dto.PurchaseEditDTO;
import com.ryazancev.config.FeignClientsConfiguration;
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
