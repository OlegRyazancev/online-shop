package com.ryazancev.product.repository;

import com.ryazancev.product.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author Oleg Ryazancev
 */

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByProductName(String name);

    Optional<List<Product>> findByOrganizationId(Long organizationId);
}
