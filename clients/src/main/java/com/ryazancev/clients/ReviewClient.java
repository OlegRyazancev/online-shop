package com.ryazancev.clients;

import com.ryazancev.config.FeignClientsConfiguration;
import com.ryazancev.dto.review.ReviewDTO;
import com.ryazancev.dto.review.ReviewPostDTO;
import com.ryazancev.dto.review.ReviewsResponse;
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
    ReviewsResponse getByProductId(
            @PathVariable("id") Long id);

    @GetMapping("api/v1/reviews/customers/{id}")
    ReviewsResponse getByCustomerId(
            @PathVariable("id") Long id);

    @PostMapping("api/v1/reviews")
    ReviewDTO create(
            @RequestBody ReviewPostDTO reviewPostDTO);

    @GetMapping("api/v1/reviews/products/{id}/average-rating")
    Double getAverageRatingByProductId(
            @PathVariable("id") Long productId);
}




