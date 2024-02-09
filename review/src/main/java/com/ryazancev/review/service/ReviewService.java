package com.ryazancev.review.service;

import com.ryazancev.dto.review.ReviewDTO;
import com.ryazancev.dto.review.ReviewPostDTO;
import com.ryazancev.dto.review.ReviewsResponse;

public interface ReviewService {

    ReviewsResponse getByCustomerId(Long customerId);

    ReviewsResponse getByProductId(Long id);

    ReviewDTO create(ReviewPostDTO reviewPostDTO);

    Double getAverageRatingByProductId(Long productId);
}
