package com.ryazancev.review.service;

import com.ryazancev.dto.review.ReviewDto;
import com.ryazancev.dto.review.ReviewEditDto;
import com.ryazancev.dto.review.ReviewsResponse;

public interface ReviewService {

    ReviewsResponse getByCustomerId(Long customerId);

    ReviewsResponse getByProductId(Long id);

    ReviewDto create(ReviewEditDto reviewEditDto);

    Double getAverageRatingByProductId(Long productId);

    void deleteByProductId(Long productId);
}
