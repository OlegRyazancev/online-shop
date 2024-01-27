package com.ryazancev.review.service;

import com.ryazancev.clients.review.ReviewDetailedDTO;
import com.ryazancev.clients.review.ReviewPostDTO;
import com.ryazancev.clients.review.ReviewsCustomerResponse;
import com.ryazancev.clients.review.ReviewsProductResponse;

public interface ReviewService {

    ReviewsCustomerResponse getByCustomerId(Long customerId);

    ReviewsProductResponse getByProductId(Long id);

    ReviewDetailedDTO create(ReviewPostDTO reviewPostDTO);
}
