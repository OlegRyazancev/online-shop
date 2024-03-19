package com.ryazancev.review.controller;

import com.ryazancev.common.dto.review.ReviewDto;
import com.ryazancev.common.dto.review.ReviewEditDto;
import com.ryazancev.common.dto.review.ReviewsResponse;
import com.ryazancev.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author Oleg Ryazancev
 */

@Slf4j
@RestController
@RequestMapping("api/v1/reviews")
@RequiredArgsConstructor
@Validated
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping("/customer/{id}")
    public ReviewsResponse getByCustomerId(
            @PathVariable("id") final Long id) {

        return reviewService.getByCustomerId(id);
    }

    @GetMapping("/product/{id}")
    public ReviewsResponse getByProductId(
            @PathVariable("id") final Long id) {

        return reviewService.getByProductId(id);
    }

    @GetMapping("/product/{id}/average-rating")
    public Double getAverageRatingByProductId(
            @PathVariable("id") final Long productId) {

        return reviewService.getAverageRatingByProductId(productId);
    }

    @PostMapping
    public ReviewDto create(
            @RequestBody final ReviewEditDto reviewEditDto) {

        return reviewService.create(reviewEditDto);
    }
}
