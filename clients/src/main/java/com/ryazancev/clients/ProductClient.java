package com.ryazancev.clients;

import com.ryazancev.config.FeignClientsConfiguration;
import com.ryazancev.dto.ProductDTO;
import com.ryazancev.dto.ProductsSimpleResponse;
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
    ProductDTO getSimpleById(
            @PathVariable("id") Long id);

    @GetMapping("api/v1/products/{id}/details")
    ProductDTO getDetailedById(
            @PathVariable("id") Long id);

    @GetMapping("api/v1/products/organizations/{organizationId}")
    ProductsSimpleResponse getProductsByOrganizationId(
            @PathVariable("organizationId") Long organizationId);

    @PutMapping("api/v1/products/{id}/update-quantity")
    ProductDTO updateQuantity(
            @PathVariable("id") Long id,
            @RequestParam("quantity") Integer quantity);

}
