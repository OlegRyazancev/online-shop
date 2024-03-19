package com.ryazancev.purchase.controller;

import com.ryazancev.common.dto.customer.CustomerPurchasesResponse;
import com.ryazancev.common.dto.purchase.PurchaseDto;
import com.ryazancev.common.dto.purchase.PurchaseEditDto;
import com.ryazancev.purchase.service.PurchaseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * @author Oleg Ryazancev
 */

@Slf4j
@RestController
@RequestMapping("api/v1/purchases")
@RequiredArgsConstructor
public class PurchaseController {

    private final PurchaseService purchaseService;

    @PostMapping
    public PurchaseDto processPurchase(
            @RequestBody final PurchaseEditDto purchaseEditDto) {

        return purchaseService.processPurchase(purchaseEditDto);
    }

    @GetMapping("/customer/{id}")
    public CustomerPurchasesResponse getByCustomerId(
            @PathVariable("id") final Long id) {

        return purchaseService.getByCustomerId(id);
    }

    @GetMapping("/{id}")
    public PurchaseDto getById(
            @PathVariable("id") final String id) {

        return purchaseService.getById(id);
    }
}
