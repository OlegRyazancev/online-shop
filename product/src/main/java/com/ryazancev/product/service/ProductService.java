package com.ryazancev.product.service;

import com.ryazancev.product.model.Product;

public interface ProductService {

    Product getById(Long productId);

    Product create(Product product);

    Product update(Product product);

    void delete(Long productId);


}
