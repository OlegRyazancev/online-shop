package com.ryazancev.product.service;

import com.ryazancev.product.model.Product;
import com.ryazancev.product.model.ProductStatus;

import java.util.List;

/**
 * @author Oleg Ryazancev
 */

public interface ProductService {

    List<Product> getAll();

    Product getById(Long id, boolean statusCheck);

    List<Product> getByOrganizationId(Long organizationId);

    Product makeRegistrationRequest(Product product);

    Product update(Product product);

    void changeStatus(Long productId, ProductStatus status);

    void register(Long productId);

    void updateQuantity(Long productId, Integer quantityInStock);

    String markProductAsDeleted(Long id);

}
