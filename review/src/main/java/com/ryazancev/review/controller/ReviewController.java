package com.ryazancev.review.controller;

import com.ryazancev.clients.review.ReviewDetailedDTO;
import com.ryazancev.clients.review.ReviewPostDTO;
import com.ryazancev.clients.review.ReviewsCustomerResponse;
import com.ryazancev.clients.review.ReviewsProductResponse;
import com.ryazancev.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("api/v1/reviews")
@RequiredArgsConstructor
@Validated
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping("/customers/{customerId}")
    public ReviewsCustomerResponse getByCustomerId(@PathVariable("customerId") Long customerId) {
        return reviewService.getByCustomerId(customerId);
    }

    @GetMapping("/products/{productId}")
    public ReviewsProductResponse getByProductId(@PathVariable("productId") Long productId) {
        return reviewService.getByProductId(productId);
    }

    @PostMapping
    public ReviewDetailedDTO create(@RequestBody ReviewPostDTO reviewPostDTO) {
        return reviewService.create(reviewPostDTO);
    }
}
