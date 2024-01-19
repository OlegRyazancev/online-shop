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
    public ResponseEntity<PurchaseDTO> processPurchase(@RequestBody PurchaseDTO purchaseDTO) {
        PurchaseDTO savedPurchase = purchaseService.processPurchase(purchaseDTO);
        return ResponseEntity.ok(savedPurchase);
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<CustomerPurchasesResponse> findByCustomerId(@PathVariable Long customerId) {
        CustomerPurchasesResponse purchases = purchaseService.getByCustomerId(customerId);
        log.info("Found purchases: {}", purchases);
        return ResponseEntity.ok(purchases);
    }

}
