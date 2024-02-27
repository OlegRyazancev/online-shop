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

    private static final Long[] customerIds = {1L, 2L, 3L, 4L, 5L};
    private static final Long[] productIds = {1L, 2L, 3L, 4L, 5L};
    private static final Double[] productPrices = {
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

        createPurchases(customerIds[0], 5);
        createPurchases(customerIds[1], 3);
        createPurchases(customerIds[2], 4);
        createPurchases(customerIds[3], 1);
    }

    private void createPurchases(Long customerId, int totalSaves) {

        LocalDateTime purchaseDate = LocalDateTime.of(2023, Month.JANUARY, 1, 0, 0, 0);

        for (int i = 0; i < totalSaves; i++) {

            purchaseRepository.save(Purchase.builder()
                    .id(String.valueOf(purchaseIdCounter++))
                    .customerId(customerId)
                    .productId(productIds[i])
                    .amount(productPrices[i])
                    .purchaseDate(purchaseDate.plusMonths(i))
                    .build());
        }
    }

}
