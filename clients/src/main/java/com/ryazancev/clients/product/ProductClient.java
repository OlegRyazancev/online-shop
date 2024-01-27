package com.ryazancev.clients.product;

import com.ryazancev.config.FeignClientsConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "product",
        configuration = FeignClientsConfiguration.class
)
public interface ProductClient {
    @GetMapping("api/v1/products/{productId}")
    ProductSimpleDTO getById(@PathVariable("productId") Long productId);

    @GetMapping("api/v1/products/{productId}/details")
    ProductDetailedDTO getDetailedById(@PathVariable("productId") Long productId);

    @GetMapping("api/v1/products/organizations/{organizationId}")
    ProductListResponse getByOrganizationId(@PathVariable("organizationId") Long organizationId);

    @PutMapping("api/v1/products/{productId}/update-quantity")
    ProductDetailedDTO updateQuantity(@PathVariable("productId") Long productId, @RequestParam("quantity") Integer quantity);

}
