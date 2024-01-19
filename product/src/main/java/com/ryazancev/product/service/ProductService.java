package com.ryazancev.product.service;

import com.ryazancev.clients.product.ProductDetailedDTO;
import com.ryazancev.clients.product.ProductListResponse;
import com.ryazancev.clients.product.ProductPostDTO;
import com.ryazancev.product.model.Product;

public interface ProductService {

    ProductListResponse getAll();

    ProductDetailedDTO getById(Long productId);

    ProductListResponse getByOrganizationId(Long organizationId);

    ProductDetailedDTO save(ProductPostDTO productPostDTO);

    //todo: update and delete products

    Product update(Product product);

    void delete(Long productId);
}
