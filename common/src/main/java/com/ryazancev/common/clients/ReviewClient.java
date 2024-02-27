package com.ryazancev.common.clients;

import com.ryazancev.common.config.FeignClientsConfiguration;
import com.ryazancev.common.dto.review.ReviewDto;
import com.ryazancev.common.dto.review.ReviewEditDto;
import com.ryazancev.common.dto.review.ReviewsResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author Oleg Ryazancev
 */

@FeignClient(
        name = "review",
        configuration = FeignClientsConfiguration.class,
        url = "${clients.review.url}"
)
public interface ReviewClient {

    @GetMapping("api/v1/reviews/product/{id}")
    ReviewsResponse getByProductId(
            @PathVariable("id") Long id);

    @GetMapping("api/v1/reviews/customer/{id}")
    ReviewsResponse getByCustomerId(
            @PathVariable("id") Long id);

    @PostMapping("api/v1/reviews")
    ReviewDto create(
            @RequestBody ReviewEditDto reviewEditDto);

    @GetMapping("api/v1/reviews/product/{id}/average-rating")
    Double getAverageRatingByProductId(
            @PathVariable("id") Long productId);

}




