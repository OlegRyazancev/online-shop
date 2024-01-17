package com.ryazancev.clients.purchase;

import com.ryazancev.clients.purchase.dto.CustomerPurchasesResponse;
import com.ryazancev.clients.purchase.dto.PurchaseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("purchase")
public interface PurchaseClient {

    @PostMapping("api/v1/purchases")
    ResponseEntity<String> processPurchase(@RequestBody PurchaseDTO purchaseDTO);

    @GetMapping("api/v1/purchases/customer/{customerId}")
    CustomerPurchasesResponse findByCustomerId(@PathVariable("customerId") Long customerId);
}
