package com.ryazancev.purchase.util;

import com.ryazancev.common.dto.customer.UpdateBalanceRequest;
import com.ryazancev.common.dto.product.UpdateQuantityRequest;
import com.ryazancev.purchase.kafka.PurchaseProducerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author Oleg Ryazancev
 */

@Service
@RequiredArgsConstructor
public class MessageProcessor {

    private final PurchaseProducerService purchaseProducerService;

    public void updateProductQuantity(Long productId,
                                      Integer availableProductsInStock) {

        purchaseProducerService.sendMessageToProductTopic(
                UpdateQuantityRequest.builder()
                        .productId(productId)
                        .quantityInStock(availableProductsInStock)
                        .build()
        );
    }

    public void updateCustomerBalance(Long customerId,
                                      Double updatedBalance) {

        purchaseProducerService.sendMessageToCustomerTopic(
                UpdateBalanceRequest.builder()
                        .customerId(customerId)
                        .balance(updatedBalance)
                        .build()
        );
    }
}
