package com.ryazancev.product.service;

import com.ryazancev.clients.product.ProductDetailedDTO;
import com.ryazancev.clients.product.ProductPostDTO;
import com.ryazancev.clients.product.ProductListResponse;
import com.ryazancev.product.model.Product;

public interface ProductService {

    ProductListResponse getAvailableProducts();

    ProductDetailedDTO getById(Long productId);

    ProductDetailedDTO save(ProductPostDTO productPostDTO);

    //todo: update and delete products
    Product update(Product product);

    void delete(Long productId);
}
