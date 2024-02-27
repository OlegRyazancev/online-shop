package com.ryazancev.common.clients;

import com.ryazancev.common.config.FeignClientsConfiguration;
import com.ryazancev.common.dto.customer.CustomerPurchasesResponse;
import com.ryazancev.common.dto.purchase.PurchaseDto;
import com.ryazancev.common.dto.purchase.PurchaseEditDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author Oleg Ryazancev
 */

@FeignClient(
        name = "purchase",
        configuration = FeignClientsConfiguration.class,
        url = "${clients.purchase.url}"
)
public interface PurchaseClient {

    @GetMapping("api/v1/purchases/customer/{id}")
    CustomerPurchasesResponse getByCustomerId(
            @PathVariable("id") Long id);

    @PostMapping("api/v1/purchases")
    PurchaseDto processPurchase(
            @RequestBody PurchaseEditDto purchaseEditDto);

    @GetMapping("api/v1/purchases/{id}")
    PurchaseDto getById(
            @PathVariable("id") String id);
}
