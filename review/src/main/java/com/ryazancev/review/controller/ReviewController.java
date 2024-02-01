package com.ryazancev.review.controller;

import com.ryazancev.dto.ReviewDTO;
import com.ryazancev.dto.ReviewPostDTO;
import com.ryazancev.dto.ReviewsResponse;
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
    public ReviewsResponse getByCustomerId(
            @PathVariable("id") Long id) {

        return reviewService.getByCustomerId(id);
    }

    @GetMapping("/products/{id}")
    public ReviewsResponse getByProductId(
            @PathVariable("id") Long id) {

        return reviewService.getByProductId(id);
    }

    @PostMapping
    public ReviewDTO create(
            @RequestBody ReviewPostDTO reviewPostDTO) {

        return reviewService.create(reviewPostDTO);
    }
}
