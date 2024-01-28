package com.ryazancev.purchase.controller;

import com.ryazancev.clients.customer.dto.CustomerPurchasesResponse;
import com.ryazancev.clients.purchase.dto.PurchaseDetailedDTO;
import com.ryazancev.clients.purchase.dto.PurchasePostDTO;
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
    public PurchaseDetailedDTO processPurchase(
            @RequestBody PurchasePostDTO purchasePostDTO) {

        return purchaseService.processPurchase(purchasePostDTO);
    }

    @GetMapping("/customer/{id}")
    public CustomerPurchasesResponse getByCustomerId(
            @PathVariable("id") Long id) {

        return purchaseService.getByCustomerId(id);
    }

}
