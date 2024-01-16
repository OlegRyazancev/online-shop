package com.ryazancev.purchase.repository;

import com.ryazancev.purchase.model.Purchase;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseRepository extends MongoRepository<Purchase, String> {

}
