package com.ryazancev.clients.product;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "product")
public interface ProductClient {
    @GetMapping("api/v1/products/organizations/{organizationId}")
    ProductListResponse getByOrganizationId(
            @PathVariable("organizationId") Long organizationId);

}
