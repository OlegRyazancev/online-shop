package com.ryazancev.clients;

import com.ryazancev.config.FeignClientsConfiguration;
import com.ryazancev.dto.product.PriceQuantityResponse;
import com.ryazancev.dto.product.ProductDto;
import com.ryazancev.dto.product.ProductsSimpleResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "product",
        configuration = FeignClientsConfiguration.class
)
public interface ProductClient {

    @GetMapping("api/v1/products/{id}/simple")
    ProductDto getSimpleById(
            @PathVariable("id") Long id);

    @GetMapping("api/v1/products/organizations/{organizationId}")
    ProductsSimpleResponse getProductsByOrganizationId(
            @PathVariable("organizationId") Long organizationId);

    @GetMapping("api/v1/products/{id}/price-quantity")
    PriceQuantityResponse getPriceAndQuantityByProductId(
            @PathVariable("id") Long productId);

    @GetMapping("api/v1/products/{id}/owner-id")
     Long getOwnerId(
            @PathVariable("id") Long productId);
}
