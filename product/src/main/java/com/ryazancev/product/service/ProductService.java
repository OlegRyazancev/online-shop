package com.ryazancev.product.service;

import com.ryazancev.clients.product.dto.ProductDTO;
import com.ryazancev.clients.product.dto.ProductEditDTO;
import com.ryazancev.clients.product.dto.ProductsSimpleListResponse;
import com.ryazancev.clients.review.dto.ReviewDetailedDTO;
import com.ryazancev.clients.review.dto.ReviewPostDTO;
import com.ryazancev.clients.review.dto.ReviewsProductResponse;

public interface ProductService {

    ProductsSimpleListResponse getAll();

    ProductDTO getSimpleById(Long id);

    ProductDTO getDetailedById(Long id);

    ProductsSimpleListResponse getByOrganizationId(Long organizationId);

    ReviewsProductResponse getReviewsByProductId(Long productId);

    ReviewDetailedDTO createReview(ReviewPostDTO reviewPostDTO);

    ProductDTO create(ProductEditDTO productEditDTO);

    ProductDTO updateQuantity(Long id, Integer quantity);

    ProductDTO update(ProductEditDTO productEditDTO);

}
