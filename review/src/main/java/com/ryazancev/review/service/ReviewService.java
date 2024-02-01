package com.ryazancev.review.service;

import com.ryazancev.dto.ReviewDTO;
import com.ryazancev.dto.ReviewPostDTO;
import com.ryazancev.dto.ReviewsResponse;

public interface ReviewService {

    ReviewsResponse getByCustomerId(Long customerId);

    ReviewsResponse getByProductId(Long id);

    ReviewDTO create(ReviewPostDTO reviewPostDTO);
}
