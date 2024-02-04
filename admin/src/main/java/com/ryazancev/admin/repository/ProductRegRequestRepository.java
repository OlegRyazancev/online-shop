package com.ryazancev.admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRegRequestRepository
        extends JpaRepository<ProductRegRequestRepository, Long> {
}
