package com.ryazancev.review.controller;

import com.ryazancev.dto.review.ReviewDTO;
import com.ryazancev.dto.review.ReviewPostDTO;
import com.ryazancev.dto.review.ReviewsResponse;
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

    @GetMapping("/customer/{id}")
    public ReviewsResponse getByCustomerId(
            @PathVariable("id") Long id) {

        return reviewService.getByCustomerId(id);
    }

    @GetMapping("/product/{id}")
    public ReviewsResponse getByProductId(
            @PathVariable("id") Long id) {

        return reviewService.getByProductId(id);
    }

    @GetMapping("/product/{id}/average-rating")
    public Double getAverageRatingByProductId(
            @PathVariable("id") Long productId) {

        return reviewService.getAverageRatingByProductId(productId);
    }

    @PostMapping
    public ReviewDTO create(
            @RequestBody ReviewPostDTO reviewPostDTO) {

        return reviewService.create(reviewPostDTO);
    }
}
