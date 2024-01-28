package com.ryazancev.product.service;

import com.ryazancev.clients.product.dto.*;
import com.ryazancev.clients.review.dto.ReviewDetailedDTO;
import com.ryazancev.clients.review.dto.ReviewPostDTO;
import com.ryazancev.clients.review.dto.ReviewsProductResponse;

public interface ProductService {

    ProductListResponse getAll();

    ProductSimpleDTO getSimpleById(Long id);

    ProductDetailedDTO getDetailedById(Long id);

    ProductListResponse getByOrganizationId(Long organizationId);

    ReviewsProductResponse getReviewsByProductId(Long productId);

    ReviewDetailedDTO createReview(ReviewPostDTO reviewPostDTO);

    ProductDetailedDTO create(ProductCreateDTO productCreateDTO);

    ProductDetailedDTO updateQuantity(Long id, Integer quantity);

    ProductDetailedDTO update(ProductUpdateDTO productUpdateDTO);

}
