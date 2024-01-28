package com.ryazancev.review.controller;

import com.ryazancev.clients.review.dto.ReviewDetailedDTO;
import com.ryazancev.clients.review.dto.ReviewPostDTO;
import com.ryazancev.clients.review.dto.ReviewsCustomerResponse;
import com.ryazancev.clients.review.dto.ReviewsProductResponse;
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

    @GetMapping("/customers/{id}")
    public ReviewsCustomerResponse getByCustomerId(
            @PathVariable("id") Long id) {

        return reviewService.getByCustomerId(id);
    }

    @GetMapping("/products/{id}")
    public ReviewsProductResponse getByProductId(
            @PathVariable("id") Long id) {

        return reviewService.getByProductId(id);
    }

    @PostMapping
    public ReviewDetailedDTO create(
            @RequestBody ReviewPostDTO reviewPostDTO) {

        return reviewService.create(reviewPostDTO);
    }
}
