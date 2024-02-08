package com.ryazancev.product.service;

import com.ryazancev.clients.params.DetailedType;
import com.ryazancev.clients.params.ReviewsType;
import com.ryazancev.dto.product.ProductDTO;
import com.ryazancev.dto.product.ProductEditDTO;
import com.ryazancev.dto.product.ProductsSimpleResponse;
import com.ryazancev.dto.product.UpdateQuantityRequest;
import com.ryazancev.dto.review.ReviewDTO;
import com.ryazancev.dto.review.ReviewPostDTO;
import com.ryazancev.product.model.ProductStatus;

public interface ProductService {

    ProductsSimpleResponse getAll();

    ProductDTO getById(Long id,
                       DetailedType detailed,
                       ReviewsType reviews);

    ProductsSimpleResponse getByOrganizationId(Long organizationId);

    ReviewDTO createReview(ReviewPostDTO reviewPostDTO);

    ProductDTO makeRegistrationRequest(ProductEditDTO productEditDTO);

    void changeStatusAndRegister(Long organizationId, ProductStatus status);

    void updateQuantity(UpdateQuantityRequest request);

    ProductDTO update(ProductEditDTO productEditDTO);




}
