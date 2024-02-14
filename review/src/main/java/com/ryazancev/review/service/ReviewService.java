package com.ryazancev.review.service;

import com.ryazancev.dto.review.ReviewDto;
import com.ryazancev.dto.review.ReviewPostDto;
import com.ryazancev.dto.review.ReviewsResponse;

public interface ReviewService {

    ReviewsResponse getByCustomerId(Long customerId);

    ReviewsResponse getByProductId(Long id);

    ReviewDto create(ReviewPostDto reviewPostDto);

    Double getAverageRatingByProductId(Long productId);

    void deleteByProductId(Long productId);
}
