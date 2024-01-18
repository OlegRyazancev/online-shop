package com.ryazancev.product.service;

import com.ryazancev.clients.ProductInfoDTO;
import com.ryazancev.clients.ProductsGetResponse;
import com.ryazancev.product.model.Product;

public interface ProductService {

    ProductInfoDTO getById(Long productId);

    Product create(Product product);

    Product update(Product product);

    void delete(Long productId);

    ProductsGetResponse getAvailableProducts();
}
