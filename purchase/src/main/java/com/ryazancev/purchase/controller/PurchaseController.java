package com.ryazancev.purchase.controller;

import com.ryazancev.clients.customer.CustomerPurchasesResponse;
import com.ryazancev.clients.purchase.PurchaseDTO;
import com.ryazancev.purchase.service.PurchaseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("api/v1/purchases")
@RequiredArgsConstructor
public class PurchaseController {

    private final PurchaseService purchaseService;
    @PostMapping
    public PurchaseDTO processPurchase(@RequestBody PurchaseDTO purchaseDTO) {
        return purchaseService.processPurchase(purchaseDTO);
    }

    @GetMapping("/customer/{customerId}")
    public CustomerPurchasesResponse findByCustomerId(@PathVariable Long customerId) {
        return purchaseService.getByCustomerId(customerId);
    }

}
