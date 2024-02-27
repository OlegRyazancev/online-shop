package com.ryazancev.purchase.repository;

import com.ryazancev.purchase.model.Purchase;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Oleg Ryazancev
 */

@Repository
public interface PurchaseRepository extends MongoRepository<Purchase, String> {
    List<Purchase> findByCustomerId(Long customerId);

}
