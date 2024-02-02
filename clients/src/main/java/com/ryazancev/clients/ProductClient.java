package com.ryazancev.clients;

import com.ryazancev.config.FeignClientsConfiguration;
import com.ryazancev.dto.product.ProductDTO;
import com.ryazancev.dto.product.ProductsSimpleResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

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

}
