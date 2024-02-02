package com.ryazancev.product.service;

import com.ryazancev.dto.product.ProductDTO;
import com.ryazancev.dto.product.ProductEditDTO;
import com.ryazancev.dto.product.ProductsSimpleResponse;
import com.ryazancev.dto.product.UpdateQuantityRequest;
import com.ryazancev.dto.review.ReviewDTO;
import com.ryazancev.dto.review.ReviewPostDTO;
import com.ryazancev.dto.review.ReviewsResponse;

public interface ProductService {

    ProductsSimpleResponse getAll();

    ProductDTO getSimpleById(Long id);

    ProductDTO getDetailedById(Long id);

    ProductsSimpleResponse getByOrganizationId(Long organizationId);

    ReviewsResponse getReviewsByProductId(Long productId);

    ReviewDTO createReview(ReviewPostDTO reviewPostDTO);

    ProductDTO create(ProductEditDTO productEditDTO);

    void updateQuantity(UpdateQuantityRequest request);

    ProductDTO update(ProductEditDTO productEditDTO);

}
