package com.ryazancev.product.service;

import com.ryazancev.dto.*;

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
