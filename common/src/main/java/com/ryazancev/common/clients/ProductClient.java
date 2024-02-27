package com.ryazancev.common.clients;

import com.ryazancev.common.config.FeignClientsConfiguration;
import com.ryazancev.common.dto.product.PriceQuantityResponse;
import com.ryazancev.common.dto.product.ProductDto;
import com.ryazancev.common.dto.product.ProductsSimpleResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author Oleg Ryazancev
 */

@FeignClient(
        name = "product",
        configuration = FeignClientsConfiguration.class,
        url = "${clients.product.url}"
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
