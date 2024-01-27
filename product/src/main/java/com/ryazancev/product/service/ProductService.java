package com.ryazancev.product.service;

import com.ryazancev.clients.product.*;
import com.ryazancev.clients.review.ReviewDetailedDTO;
import com.ryazancev.clients.review.ReviewPostDTO;
import com.ryazancev.clients.review.ReviewProductDTO;
import com.ryazancev.clients.review.ReviewsProductResponse;

public interface ProductService {

    ProductListResponse getAll();

    ProductDTO getById(Long productId);

    ProductDetailedDTO getDetailedById(Long productId);

    ProductListResponse getByOrganizationId(Long organizationId);

    ProductDetailedDTO create(ProductCreateDTO productCreateDTO);

    ProductDetailedDTO updateQuantity(Long productId, Integer quantity);

    ProductDetailedDTO update(ProductUpdateDTO productUpdateDTO);

    ReviewsProductResponse getReviewsByProductId(Long id);

    ReviewDetailedDTO createReview(ReviewPostDTO reviewPostDTO);
}
