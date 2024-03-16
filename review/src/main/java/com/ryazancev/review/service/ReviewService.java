package com.ryazancev.review.service;

import com.ryazancev.common.dto.review.ReviewDto;
import com.ryazancev.common.dto.review.ReviewEditDto;
import com.ryazancev.common.dto.review.ReviewsResponse;

/**
 * @author Oleg Ryazancev
 */

public interface ReviewService {

    ReviewsResponse getByCustomerId(Long customerId);

    ReviewsResponse getByProductId(Long id);

    ReviewDto create(ReviewEditDto reviewEditDto);

    Double getAverageRatingByProductId(Long productId);

    String deleteByProductId(Long productId);
}
