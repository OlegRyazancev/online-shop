package com.ryazancev.clients.review;

import com.ryazancev.clients.review.dto.ReviewDetailedDTO;
import com.ryazancev.clients.review.dto.ReviewPostDTO;
import com.ryazancev.clients.review.dto.ReviewsProductResponse;
import com.ryazancev.config.FeignClientsConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "review",
        configuration = FeignClientsConfiguration.class
)
public interface ReviewClient {

    @GetMapping("api/v1/reviews/products/{id}")
    ReviewsProductResponse getByProductId(
            @PathVariable("id") Long id);

    @PostMapping("api/v1/reviews")
    ReviewDetailedDTO create(
            @RequestBody ReviewPostDTO reviewPostDTO);

}
