package com.ryazancev.purchase.controller;

import com.ryazancev.clients.purchase.dto.CustomerPurchasesResponse;
import com.ryazancev.clients.purchase.dto.PurchaseDTO;
import com.ryazancev.purchase.model.Purchase;
import com.ryazancev.purchase.service.PurchaseService;
import com.ryazancev.purchase.util.mappers.PurchaseMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("api/v1/purchases")
@RequiredArgsConstructor
public class PurchaseController {

    private final PurchaseService purchaseService;
    private final PurchaseMapper purchaseMapper;

    @PostMapping
    public ResponseEntity<String> processPurchase(@RequestBody PurchaseDTO purchaseDTO) {
        Purchase purchase = purchaseMapper.toEntity(purchaseDTO);
        Purchase savedPurchase = purchaseService.processPurchase(purchase);
        log.info("Purchase saved with id: {} and date: {}", savedPurchase.getId(), savedPurchase.getPurchaseDate());
        return ResponseEntity.ok("Purchase successfully saved");
    }

    @GetMapping("/customer/{customerId}")
    public CustomerPurchasesResponse findByCustomerId(@PathVariable Long customerId) {
        List<Purchase> purchases = purchaseService.getByCustomerId(customerId);
        log.info("Found purchases: {}", purchases);
        return CustomerPurchasesResponse.builder()
                .purchases(purchaseMapper.toDTO(purchases))
                .build();
    }

}
