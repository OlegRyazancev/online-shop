package com.ryazancev.clients.product;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "product")
public interface ProductClient {
    @GetMapping("api/v1/products/organizations/{organizationId}")
    ProductListResponse getByOrganizationId(@PathVariable("organizationId") Long organizationId);

    @GetMapping("api/v1/products/{productId}")
    ProductDetailedDTO getInfoById(@PathVariable("productId") Long productId);

    @PutMapping("api/v1/products/{productId}/update-quantity")
    ProductDetailedDTO updateQuantity(@PathVariable("productId") Long productId, @RequestParam("quantity") Integer quantity);

    @GetMapping("api/v1/products/{productId}/check-organization")
    Boolean isOrganizationProduct(@PathVariable("productId") Long productId, @RequestParam("organizationId") Long organizationId);

    @PutMapping("api/v1/products")
    ProductDetailedDTO update(@RequestBody ProductUpdateDTO productUpdateDTO);
}
