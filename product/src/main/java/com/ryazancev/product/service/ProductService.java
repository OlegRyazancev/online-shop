package com.ryazancev.product.service;

import com.ryazancev.product.model.Product;
import com.ryazancev.product.model.ProductStatus;

import java.util.List;

public interface ProductService {

    List<Product> getAll();

    Product getById(Long id, boolean statusCheck);

    List<Product> getByOrganizationId(Long organizationId);

    Product makeRegistrationRequest(Product product);

    Product update(Product product);

    void changeStatusAndRegister(Long organizationId, ProductStatus status);

    void updateQuantity(Long productId, Integer quantityInStock);

    void markProductAsDeleted(Long id);
}
