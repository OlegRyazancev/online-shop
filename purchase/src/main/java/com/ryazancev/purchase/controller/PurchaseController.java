package com.ryazancev.purchase.controller;

import com.ryazancev.dto.CustomerPurchasesResponse;
import com.ryazancev.dto.PurchaseDTO;
import com.ryazancev.dto.PurchaseEditDTO;
import com.ryazancev.purchase.service.PurchaseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("api/v1/purchases")
@RequiredArgsConstructor
public class PurchaseController {

    private final PurchaseService purchaseService;

    @PostMapping
    public PurchaseDTO processPurchase(
            @RequestBody PurchaseEditDTO purchaseEditDTO) {

        return purchaseService.processPurchase(purchaseEditDTO);
    }

    @GetMapping("/customer/{id}")
    public CustomerPurchasesResponse getByCustomerId(
            @PathVariable("id") Long id) {

        return purchaseService.getByCustomerId(id);
    }

}
