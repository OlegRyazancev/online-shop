package com.ryazancev.product.service;

import com.ryazancev.clients.product.*;

public interface ProductService {

    ProductListResponse getAll();

    ProductDTO getById(Long productId);

    ProductDetailedDTO getDetailedById(Long productId);

    ProductListResponse getByOrganizationId(Long organizationId);

    ProductDetailedDTO create(ProductCreateDTO productCreateDTO);

    ProductDetailedDTO updateQuantity(Long productId, Integer quantity);

    ProductDetailedDTO update(ProductUpdateDTO productUpdateDTO);

}
