package com.ryazancev.product.service;

import com.ryazancev.clients.product.*;
import com.ryazancev.clients.review.ReviewDetailedDTO;
import com.ryazancev.clients.review.ReviewPostDTO;
import com.ryazancev.clients.review.ReviewsProductResponse;

public interface ProductService {

    ProductListResponse getAll();

    ProductSimpleDTO getSimpleById(Long id);

    ProductDetailedDTO getDetailedById(Long id);

    ProductListResponse getByOrganizationId(Long id);

    ReviewsProductResponse getReviewsByProductId(Long id);

    ReviewDetailedDTO createReview(ReviewPostDTO reviewPostDTO);

    ProductDetailedDTO create(ProductCreateDTO productCreateDTO);

    ProductDetailedDTO updateQuantity(Long id, Integer quantity);

    ProductDetailedDTO update(ProductUpdateDTO productUpdateDTO);

}
