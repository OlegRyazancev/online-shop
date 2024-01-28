package com.ryazancev.clients.product;

import com.ryazancev.clients.product.dto.ProductDetailedDTO;
import com.ryazancev.clients.product.dto.ProductListResponse;
import com.ryazancev.clients.product.dto.ProductSimpleDTO;
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
    @GetMapping("api/v1/products/{id}")
    ProductSimpleDTO getSimpleProductById(
            @PathVariable("id") Long id);

    @GetMapping("api/v1/products/{id}/details")
    ProductDetailedDTO getDetailedProductById(
            @PathVariable("id") Long id);

    @GetMapping("api/v1/products/organizations/{organizationId}")
    ProductListResponse getProductsByOrganizationId(
            @PathVariable("organizationId") Long organizationId);

    @PutMapping("api/v1/products/{id}/update-quantity")
    ProductDetailedDTO updateQuantity(
            @PathVariable("id") Long id,
            @RequestParam("quantity") Integer quantity);

}
