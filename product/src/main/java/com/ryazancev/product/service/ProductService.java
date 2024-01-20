package com.ryazancev.product.service;

import com.ryazancev.clients.product.ProductDetailedDTO;
import com.ryazancev.clients.product.ProductListResponse;
import com.ryazancev.clients.product.ProductCreateDTO;

public interface ProductService {

    ProductListResponse getAll();

    ProductDetailedDTO getById(Long productId);

    ProductListResponse getByOrganizationId(Long organizationId);

    ProductDetailedDTO create(ProductCreateDTO productCreateDTO);

    ProductDetailedDTO updateQuantity(Long productId, Integer quantity);

    //todo: update and delete products


    void delete(Long productId);
}
