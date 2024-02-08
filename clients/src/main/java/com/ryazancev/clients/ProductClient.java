package com.ryazancev.clients;

import com.ryazancev.config.FeignClientsConfiguration;
import com.ryazancev.dto.product.ProductDTO;
import com.ryazancev.dto.product.ProductsSimpleResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "product",
        configuration = FeignClientsConfiguration.class
)
public interface ProductClient {

    @GetMapping("api/v1/products/{id}")
    ProductDTO getById(
            @PathVariable("id") Long id,
            @RequestParam(
                    value = "detailLevel",
                    defaultValue = "simple") String detailLevel,
            @RequestParam(
                    value = "includeReviews",
                    defaultValue = "no_reviews") String includeReviews);

    @GetMapping("api/v1/products/organizations/{organizationId}")
    ProductsSimpleResponse getProductsByOrganizationId(
            @PathVariable("organizationId") Long organizationId);

}
