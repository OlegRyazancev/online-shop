package com.ryazancev.purchase.util.processor;

import com.ryazancev.common.dto.customer.UpdateBalanceRequest;
import com.ryazancev.common.dto.product.UpdateQuantityRequest;
import com.ryazancev.purchase.kafka.PurchaseProducerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @author Oleg Ryazancev
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaMessageProcessor {

    private final PurchaseProducerService purchaseProducerService;

    @Async("asyncTaskExecutor")
    public void updateProductQuantity(final Long productId,
                                      final Integer availableProductsInStock) {

        log.info("Method updateProductQuantity starts work at "
                + "thread: " + Thread.currentThread().getName());

        purchaseProducerService.sendMessageToProductTopic(
                UpdateQuantityRequest.builder()
                        .productId(productId)
                        .quantityInStock(availableProductsInStock)
                        .build()
        );
    }

    @Async("asyncTaskExecutor")
    public void updateCustomerBalance(final Long customerId,
                                      final Double updatedBalance) {

        log.info("Method updateCustomerBalance starts work at "
                + "thread: " + Thread.currentThread().getName());

        purchaseProducerService.sendMessageToCustomerTopic(
                UpdateBalanceRequest.builder()
                        .customerId(customerId)
                        .balance(updatedBalance)
                        .build()
        );
    }
}
