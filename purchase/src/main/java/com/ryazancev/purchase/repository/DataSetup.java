package com.ryazancev.purchase.repository;

import com.ryazancev.purchase.model.Purchase;
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

    private final PurchaseRepository purchaseRepository;

    private static final Long[] CUSTOMER_IDS = {1L, 2L, 3L, 4L, 5L};
    private static final Long[] PRODUCT_IDS = {1L, 2L, 3L, 4L, 5L};
    private static final Double[] PRODUCT_PRICES = {
            1299.99,
            699.99,
            79.99,
            149.99,
            199.99
    };

    private static int purchaseIdCounter = 1;


    @PostConstruct
    public void setup() {
        purchaseRepository.deleteAll();

        createPurchases(CUSTOMER_IDS[0], 5);
        createPurchases(CUSTOMER_IDS[1], 3);
        createPurchases(CUSTOMER_IDS[2], 4);
        createPurchases(CUSTOMER_IDS[3], 1);
    }

    private void createPurchases(final Long customerId,
                                 final int totalSaves) {

        LocalDateTime purchaseDate =
                LocalDateTime.of(2023, Month.JANUARY, 1, 0, 0, 0);

        for (int i = 0; i < totalSaves; i++) {

            purchaseRepository.save(Purchase.builder()
                    .id(String.valueOf(purchaseIdCounter++))
                    .customerId(customerId)
                    .productId(PRODUCT_IDS[i])
                    .amount(PRODUCT_PRICES[i])
                    .purchaseDate(purchaseDate.plusMonths(i))
                    .build());
        }
    }

}
