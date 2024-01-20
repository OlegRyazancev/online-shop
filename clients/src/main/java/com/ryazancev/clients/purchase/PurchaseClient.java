package com.ryazancev.clients.purchase;

import com.ryazancev.clients.customer.CustomerPurchasesResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "purchase")
public interface PurchaseClient {

    @PostMapping("api/v1/purchases")
    PurchasePostDTO processPurchase(@RequestBody PurchasePostDTO purchasePostDTO);

    @GetMapping("api/v1/purchases/customer/{customerId}")
    CustomerPurchasesResponse findByCustomerId(@PathVariable("customerId") Long customerId);
}
