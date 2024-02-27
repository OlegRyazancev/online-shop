package com.ryazancev.review.repository;

import com.ryazancev.review.model.Review;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author Oleg Ryazancev
 */

@Repository
public interface ReviewRepository extends MongoRepository<Review, String> {
    List<Review> findByCustomerId(Long customerId);

    List<Review> findByProductId(Long id);

    Optional<Review> findByPurchaseId(String id);
}
