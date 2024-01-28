package com.ryazancev.review.service;

import com.ryazancev.clients.review.dto.ReviewDTO;
import com.ryazancev.clients.review.dto.ReviewPostDTO;
import com.ryazancev.clients.review.dto.ReviewsResponse;

public interface ReviewService {

    ReviewsResponse getByCustomerId(Long customerId);

    ReviewsResponse getByProductId(Long id);

    ReviewDTO create(ReviewPostDTO reviewPostDTO);
}
