package com.ryazancev.review.service;

import com.ryazancev.clients.review.dto.ReviewDetailedDTO;
import com.ryazancev.clients.review.dto.ReviewPostDTO;
import com.ryazancev.clients.review.dto.ReviewsCustomerResponse;
import com.ryazancev.clients.review.dto.ReviewsProductResponse;

public interface ReviewService {

    ReviewsCustomerResponse getByCustomerId(Long customerId);

    ReviewsProductResponse getByProductId(Long id);

    ReviewDetailedDTO create(ReviewPostDTO reviewPostDTO);
}
