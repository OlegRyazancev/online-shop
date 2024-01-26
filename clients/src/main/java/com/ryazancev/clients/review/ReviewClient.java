package com.ryazancev.clients.review;

import com.ryazancev.config.FeignClientsConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "review",
        configuration = FeignClientsConfiguration.class
)
public interface ReviewClient {

    @GetMapping("api/v1/reviews/products/{productId}")
    ReviewsProductResponse getByProductId(@PathVariable("productId") Long productId);

}
