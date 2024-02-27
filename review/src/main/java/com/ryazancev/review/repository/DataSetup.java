package com.ryazancev.review.repository;

import com.ryazancev.review.model.Review;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.Month;

/**
 * @author Oleg Ryazancev
 */

@Component
@RequiredArgsConstructor
public class DataSetup {


    private final ReviewRepository reviewRepository;
    private static final long[] customerIds = {1L, 2L, 3L, 4L, 5L};
    private static final long[] productIds = {1L, 2L, 3L, 4L, 5L};
    private static final String[] purchasesIds = new String[13];
    private static int reviewIdCounter = 1;

    static {
        for (int i = 0; i < purchasesIds.length; i++) {
            purchasesIds[i] = String.valueOf(i + 1);
        }
    }


    @PostConstruct
    public void setup() {

        reviewRepository.deleteAll();

        createReview(purchasesIds[0], 5);
        createReview(purchasesIds[1], 4);
        createReview(purchasesIds[2], 3);
        createReview(purchasesIds[3], 2);
        createReview(purchasesIds[4], 1);
        createReview(purchasesIds[5], 2);
        createReview(purchasesIds[6], 3);
        createReview(purchasesIds[7], 4);
        createReview(purchasesIds[8], 5);
        createReview(purchasesIds[9], 1);
        createReview(purchasesIds[10], 1);
        createReview(purchasesIds[11], 2);
    }

    private void createReview(String purchaseId, int totalSaves) {

        LocalDateTime createdAt = LocalDateTime.of(2023, Month.JANUARY, 1, 0, 0, 0);

        for (int i = 0; i < totalSaves; i++) {

            reviewRepository.save(Review.builder()
                    .id(String.valueOf(reviewIdCounter++))
                    .productId(productIds[i])
                    .customerId(customerIds[i])
                    .body("Example body")
                    .rating(i + 1)
                    .purchaseId(purchaseId)
                    .createdAt(createdAt.plusMonths(i))
                    .build());
        }
    }
}