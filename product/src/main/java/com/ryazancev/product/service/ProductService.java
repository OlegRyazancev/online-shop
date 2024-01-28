package com.ryazancev.product.service;

import com.ryazancev.clients.product.dto.ProductDTO;
import com.ryazancev.clients.product.dto.ProductEditDTO;
import com.ryazancev.clients.product.dto.ProductsSimpleResponse;
import com.ryazancev.clients.review.dto.ReviewDTO;
import com.ryazancev.clients.review.dto.ReviewPostDTO;
import com.ryazancev.clients.review.dto.ReviewsResponse;

public interface ProductService {

    ProductsSimpleResponse getAll();

    ProductDTO getSimpleById(Long id);

    ProductDTO getDetailedById(Long id);

    ProductsSimpleResponse getByOrganizationId(Long organizationId);

    ReviewsResponse getReviewsByProductId(Long productId);

    ReviewDTO createReview(ReviewPostDTO reviewPostDTO);

    ProductDTO create(ProductEditDTO productEditDTO);

    ProductDTO updateQuantity(Long id, Integer quantity);

    ProductDTO update(ProductEditDTO productEditDTO);

}
